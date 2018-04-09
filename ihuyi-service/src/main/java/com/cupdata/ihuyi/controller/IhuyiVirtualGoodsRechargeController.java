package com.cupdata.ihuyi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.api.ihuyi.IhuyiVirtualGoodsController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.*;
import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.CacheFeignClient;
import com.cupdata.ihuyi.feign.NotifyFeignClient;
import com.cupdata.ihuyi.feign.OrderFeignClient;
import com.cupdata.ihuyi.feign.ProductFeignClient;
import com.cupdata.ihuyi.utils.IhuyiUtils;
import com.cupdata.ihuyi.vo.GameRechargeVo;
import com.cupdata.ihuyi.vo.IhuyiRechargeRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 互亿虚拟商品充值业务
 * @CreateDate: 2018/3/8 20:29
 */
@Slf4j
@RestController
public class IhuyiVirtualGoodsRechargeController implements IhuyiVirtualGoodsController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    @Autowired
    private CacheFeignClient cacheFeignClient ;

    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value="org", required=true) String org, @RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
        log.info("开始互亿虚拟充值**********************************************");
        log.info("调用互亿虚拟商品充值controller......org:"+org+",Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrderDesc:"+rechargeReq.getOrderDesc());
        //设置响应结果
        BaseResponse<RechargeRes> rechargeRes = new BaseResponse<RechargeRes>();
        try {
            //step1.根据服务产品编号查询对应的服务产品
            log.info("互亿虚拟产品充值,根据产品编号查询产品信息...ProductNo:"+rechargeReq.getProductNo());
            BaseResponse<ProductInfVo> productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            //产品信息响应码失败,返回错误信息参数
            if (productInfo == null || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                log.info("获取产品失败");
                //产品查询失败，设置错误的响应码和响应信息，给予返回
                rechargeRes.setResponseCode(productInfo.getResponseCode());
                rechargeRes.setResponseMsg(productInfo.getResponseMsg());
                return rechargeRes;
            }

            //step2.创建充值订单：订单状态已完成初始化
            CreateRechargeOrderVo createRechargeOrderVo = new CreateRechargeOrderVo();
            createRechargeOrderVo.setOrderDesc(rechargeReq.getOrderDesc());
            createRechargeOrderVo.setOrgNo(org);
            createRechargeOrderVo.setOrgOrderNo(rechargeReq.getOrgOrderNo());
            createRechargeOrderVo.setProductNo(rechargeReq.getProductNo());

            //step3.调用订单服务创建订单
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData()
                    || null == rechargeOrderRes.getData().getOrder()
                    || null == rechargeOrderRes.getData().getRechargeOrder()){
                //创建订单失败，设置响应错误消息和错误状态码，给予返回
                log.info("创建订单失败");
                rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return rechargeRes;
            }
            log.info("创建订单成功,订单编号:"+rechargeOrderRes.getData().getOrder().getOrderNo());

            //通过请求参数判断为游戏充值(通过判断充值子订单中gameRegin和gameServer字段是否为空来判断是否为游戏充值)
            GameRechargeVo gameRechargeVo = new GameRechargeVo();
            if (!StringUtils.isEmpty(rechargeReq.getGameRegion())) {
                log.info("游戏充值大区:"+rechargeReq.getGameRegion());
                gameRechargeVo.setGameRegion(rechargeReq.getGameRegion());
            }

            if (!StringUtils.isEmpty(rechargeReq.getGameServer())){
                log.info("游戏充值服务器:"+rechargeReq.getGameServer());
                gameRechargeVo.setGameServer(rechargeReq.getGameServer());
            }

            String extend = JSONObject.toJSONString(gameRechargeVo);

            //step4.调用互亿充值工具类进行虚拟产品充值(订单编号，充值账号，商户业务参数,充值件数,扩展参数,买家ip)
            log.info("调用互亿工具类进行虚拟充值");
            IhuyiRechargeRes ihuyiRechargeRes = IhuyiUtils.ihuyiVirtualGoodsRechargeBuy(rechargeOrderRes.getData().getOrder().getOrderNo(),rechargeReq.getAccount(),productInfo.getData().getProduct().getSupplierParam(),rechargeReq.getRechargeNumber(),extend,"",cacheFeignClient);
            RechargeRes res = new RechargeRes();
            //1:提交成功； 0、1015、1016、4001：核单处理,订购成功
            if (1 == ihuyiRechargeRes.getCode() || 0 == ihuyiRechargeRes.getCode()
                    || 1015 == ihuyiRechargeRes.getCode()
                    || 1016 == ihuyiRechargeRes.getCode()
                    || 4001 == ihuyiRechargeRes.getCode()) {
                log.info("互亿虚拟充值下单成功,互亿充值结果状态码:"+ihuyiRechargeRes.getCode());
                rechargeOrderRes.getData().getRechargeOrder().setGameRegion(rechargeReq.getGameRegion());  //游戏大区
                rechargeOrderRes.getData().getRechargeOrder().setGameServer(rechargeReq.getGameServer());  //游戏服务名称
                rechargeOrderRes.getData().getRechargeOrder().setAccountNumber(rechargeReq.getAccount());  //充值账号
                rechargeOrderRes.getData().getRechargeOrder().setRechargeNumber(rechargeReq.getRechargeNumber()); //充值数量
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_HANDING); //订单状态:处理中
                rechargeOrderRes.getData().getOrder().setNotifyUrl(rechargeReq.getNotifyUrl());            //充值结果通知地址(机构接收地址)
                if (!StringUtils.isEmpty(ihuyiRechargeRes.getTaskid())) {
                    rechargeOrderRes.getData().getOrder().setSupplierOrderNo(ihuyiRechargeRes.getTaskid());
                }

                //调用订单服务更新订单
                log.info("下单成功,更新订单,订单编号:"+rechargeOrderRes.getData().getOrder().getOrderNo());
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData()
                        || null == rechargeOrderRes.getData().getOrder()
                        || null == rechargeOrderRes.getData().getRechargeOrder()) {
                    rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return rechargeRes;
                }

                //虚拟充值下单成功响应结果
                log.info("下单成功,响应信息");
                res.setRechargeStatus(ModelConstants.RECHARGE_ING);
                res.setOrderNo(rechargeOrderRes.getData().getOrder().getOrderNo());
                rechargeRes.setData(res);
                return rechargeRes;
            } else {
                //充值失败响应结果
                log.info("互亿虚拟商品充值失败");
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL); //订单失败
                rechargeOrderRes.getData().getOrder().setOrderFailDesc(IhuyiRechargeResCode.FAIL_VIRTUAL_RECHARGE.getMsg());
                //调用订单服务更新订单
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData()
                        || null == rechargeOrderRes.getData().getOrder()
                        || null == rechargeOrderRes.getData().getRechargeOrder()) {
                    rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return rechargeRes;
                }

                //互亿虚拟充值下单失败,通知机构
                notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderRes.getData().getOrder().getOrderNo());

                //设置响应结果
                rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
                rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
                res.setRechargeStatus(ModelConstants.RECHARGE_FIAL);
                rechargeRes.setData(res);
                return rechargeRes;
            }
        }catch (Exception e){
            log.info("互亿虚商品充值出现异常,异常信息:"+e.getMessage());
            e.printStackTrace();
            rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
            rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
            return rechargeRes;
        }
    }

    /**
     * 互亿虚拟商品充值回调函数
     * @param request
     * @param response
     * @throws IOException
     */
    public void ihuyiVirtualRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("互亿虚拟充值有新的推送通知消息...");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String resultStr = "success";
        String taskid = request.getParameter("taskid");
        String orderid = request.getParameter("orderid");
        String account = request.getParameter("account");
        String status = request.getParameter("status");
        String return_ = request.getParameter("return");
        String money = request.getParameter("money");
        String sign = request.getParameter("sign");
        Map<String, String> map = new HashMap();
        map.put("taskid", taskid);
        map.put("orderid", orderid);
        map.put("account", account);
        map.put("status", status);
        map.put("return", return_);
        map.put("money", money);
        String validateSign = IhuyiUtils.getSign(map,cacheFeignClient);
        map.put("sign", sign);
        log.info("互亿推送虚拟充值结果...互亿虚拟商品订购回调请求参数："+ JSON.toJSONString(map));
        if (validateSign.equals(sign)) {
            log.info("互亿推送虚拟充值结果...互亿虚拟商品购买回调，验签通过");
            //根据订单编号查询主订单和充值订单
            BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrderNo(orderid);

            //如果商户订单号为空，就添加商户订单号
            if (StringUtils.isEmpty(rechargeOrderVo.getData().getOrder().getSupplierOrderNo())) {
                rechargeOrderVo.getData().getOrder().setSupplierOrderNo(taskid);
                orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新商户订单号
            }

            if ("2".equals(status)) {//充值成功
                if (rechargeOrderVo.getData().getOrder() != null && ModelConstants.ORDER_STATUS_HANDING.equals(rechargeOrderVo.getData().getOrder().getOrderStatus())) {
                    rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);     //订单状态改为成功
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());                              //更新订单状态
                    log.info("互亿推送虚拟充值结果...互亿虚拟商品购买成功");
                    writer.print(resultStr);
                    //购买成功,通知机构
                    //notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
                } else if (rechargeOrderVo.getData().getOrder() == null){
                    log.info("互亿推送虚拟充值结果...互亿虚拟商品订购状态的订单号不存在");
                    writer.print(resultStr);
                } else {
                    log.info("互亿推送虚拟充值结果...互亿虚拟商品订购,状态已处理");
                    writer.print(resultStr);
                }
            } else {
                log.info("互亿推送虚拟充值结果...虚拟充值失败,订单状态更新为失败");
                rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);
                orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                resultStr = "fail";
                writer.print(resultStr);
                //购买失败,通知机构
                notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
            }
        } else {
            log.info("互亿推送虚拟充值结果...互亿虚拟商品订购回调，验签失败");
            resultStr = "fail";
            writer.print(resultStr);
        }
        log.info("互亿推送虚拟充值结果...互亿虚拟商品订购回调返回字符：" + resultStr);
    }
}
