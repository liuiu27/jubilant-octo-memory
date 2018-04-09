package com.cupdata.ihuyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.api.ihuyi.IHuyiTrafficController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.commons.vo.recharge.CreateRechargeOrderVo;
import com.cupdata.commons.vo.recharge.RechargeReq;
import com.cupdata.commons.vo.recharge.RechargeRes;
import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.CacheFeignClient;
import com.cupdata.ihuyi.feign.NotifyFeignClient;
import com.cupdata.ihuyi.feign.OrderFeignClient;
import com.cupdata.ihuyi.feign.ProductFeignClient;
import com.cupdata.ihuyi.utils.IhuyiUtils;
import com.cupdata.ihuyi.vo.IhuyiRechargeRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 互亿流量充值业务
 * @CreateDate: 2018/3/6 15:59
 */
@Slf4j
@RestController
public class IhuyiTrafficRechargeController implements IHuyiTrafficController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    @Autowired
    private CacheFeignClient cacheFeignClient ;

    /**
     * 互亿流量充值
     * @param org
     * @param rechargeReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value="org", required=true) String org,@RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {
        log.info("互亿流量充值**********************************************");
        log.info("进入互亿流量充值controller......Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrderDesc:"+rechargeReq.getOrderDesc()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        //设置响应结果
        BaseResponse<RechargeRes> rechargeRes = new BaseResponse<RechargeRes>();
        try {
            //step1.根据服务产品编号查询对应的服务产品
            log.info("根据产品编号查询产品信息...ProductNo:"+rechargeReq.getProductNo());
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
            createRechargeOrderVo.setAccountNumber(rechargeReq.getAccount());

            //调用订单服务创建订单
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

            //step3.调用互亿充值工具类进行流量充值
            log.info("调用互亿工具类进行流量充值");
            IhuyiRechargeRes ihuyiRechargeRes = IhuyiUtils.ihuyiTrafficRecharge(rechargeOrderRes.getData(),rechargeReq,cacheFeignClient);
            RechargeRes res = new RechargeRes();
            //1:提交成功； 0、1015、1016、4001：核单处理,订购成功
            if (1 == ihuyiRechargeRes.getCode() || 0 == ihuyiRechargeRes.getCode()
                    || 1015 == ihuyiRechargeRes.getCode()
                    || 1016 == ihuyiRechargeRes.getCode()
                    || 4001 == ihuyiRechargeRes.getCode()) {
                log.info("互亿流量充值下单成功,互亿充值结果状态码:"+ihuyiRechargeRes.getCode());
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_HANDING);  //处理中
                rechargeOrderRes.getData().getOrder().setNotifyUrl(rechargeReq.getNotifyUrl());             //充值结果通知地址(机构接收地址)
                if (!StringUtils.isEmpty(ihuyiRechargeRes.getTaskid())) {
                    rechargeOrderRes.getData().getOrder().setSupplierOrderNo(ihuyiRechargeRes.getTaskid()); //商户订单号
                }

                //调用订单服务更新订单
                log.info("互亿流量充值下单成功更新订单,订单编号:"+rechargeOrderRes.getData().getOrder().getOrderNo());
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData()
                        || null == rechargeOrderRes.getData().getOrder()
                        || null == rechargeOrderRes.getData().getRechargeOrder()) {
                    rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return rechargeRes;
                }

                //互亿流量下单成功响应结果
                res.setRechargeStatus(ModelConstants.RECHARGE_ING);
                rechargeRes.setData(res);
                return rechargeRes;
            } else {
                //下单失败响应结果,并通知机构
                log.info("互亿话费充值下单失败");
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL); //订单失败
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

                //充值失败,通知机构下单失败
                notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderRes.getData().getOrder().getOrderNo());

                //设置响应结果
                rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
                rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
                res.setRechargeStatus(ModelConstants.RECHARGE_FIAL);
                rechargeRes.setData(res);
                return rechargeRes;
            }
        }catch (Exception e){
            e.printStackTrace();
            rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
            rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
            return rechargeRes;
        }
    }

    /**
     * 互亿流量订购接口回调(此接口用于互亿调用,以此告知SIP流量充值的最终结果)
     * @param request
     * @param response
     * @throws IOException
     */
    public void ihuyiTrafficRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("互亿流量充值结果有新的推送通知消息...");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String resultStr = "success";
        try {
            String taskid = request.getParameter("taskid");
            String orderid = request.getParameter("orderid");
            String mobile = request.getParameter("mobile");
            String message = request.getParameter("message");
            String status = request.getParameter("status");
            String sign = request.getParameter("sign");
            Map<String, String> map = new HashMap();
            map.put("taskid", taskid);
            map.put("mobile", mobile);
            map.put("status", status);//流量充值
            map.put("message", message);
            String validateSign = IhuyiUtils.getSign(map,cacheFeignClient);
            map.put("orderid",orderid);
            map.put("sign",sign);
            log.info("互亿流量充值回调，互亿请求数据:" + JSONObject.toJSONString(map));
            if (validateSign.equals(sign)) {
                log.info("互亿流量充值回调，验签通过");
                BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrderNo(orderid);
                if (null != rechargeOrderVo.getData().getOrder() && !ModelConstants.ORDER_MERCHANT_FLAG_IHUYI.equals(rechargeOrderVo.getData().getOrder().getSupplierFlag())) {
                    writer.print(resultStr);
                    return;
                }

                //如果商户订单号为空，就添加商户订单号
                if (org.apache.commons.lang.StringUtils.isEmpty(rechargeOrderVo.getData().getOrder().getSupplierOrderNo())) {
                    rechargeOrderVo.getData().getOrder().setSupplierOrderNo(taskid);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData()); //更新商户订单
                }

                if ("1".equals(status)) {  //充值成功
                    if (rechargeOrderVo.getData().getOrder() != null && ModelConstants.ORDER_STATUS_HANDING.equals(rechargeOrderVo.getData().getOrder().getOrderStatus())) {
                        rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                        orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                        log.info("互亿流量充值状态推送...互亿话费充值订单更新成功");
                        writer.print(resultStr);
                        //互亿流量充值成功,通知机构
                        notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
                    } else if (rechargeOrderVo.getData().getOrder() == null){
                        log.info("互亿流量充值状态推送...互亿话费订购状态的订单号不存在");
                        writer.print(resultStr);
                    } else {
                        log.info("互亿流量充值状态推送...互亿话费流量订购,状态已处理");
                        writer.print(resultStr);
                    }
                } else { //充值失败
                    log.info("互亿流量充值状态推送...话费充值失败,订单状态更新为失败");
                    rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                    resultStr = "fail";
                    writer.print(resultStr);
                    //充值失败,通知机构
                    notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
                }
            } else {
                log.info("互亿流量充值状态推送...互亿流量充值回调，验签失败");
                resultStr = "fail";
                writer.print(resultStr);
            }
        } catch (Exception e) {
            log.info("",e);
            resultStr = "fail";
            writer.print(resultStr);
        }
        log.info("互亿流量充值状态推送...互亿流量充值回调返回字符：" + resultStr);
    }
}
