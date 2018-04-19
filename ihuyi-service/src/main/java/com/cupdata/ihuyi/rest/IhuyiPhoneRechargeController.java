package com.cupdata.ihuyi.rest;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.ConfigFeignClient;
import com.cupdata.ihuyi.feign.OrderFeignClient;
import com.cupdata.ihuyi.feign.OrgFeignClient;
import com.cupdata.ihuyi.feign.ProductFeignClient;
import com.cupdata.ihuyi.utils.IhuyiUtils;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.ihuyi.IhuyiPhoneController;
import com.cupdata.sip.common.api.ihuyi.response.IhuyiRechargeRes;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.api.recharge.response.RechargeRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.RechargeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: DingCong
 * @Description: 互亿话费充值相关业务处理
 * @@Date: Created in 10:06 2018/4/19
 */
@Slf4j
@RestController
public class IhuyiPhoneRechargeController implements IhuyiPhoneController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient ;

    @Autowired
    private OrgFeignClient orgFeignClient;


    /**
     * 互亿话费充值Controller
     * @param org
     * @param rechargeReq
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(String org, RechargeReq rechargeReq) {
        log.info("调用互亿话费充值Controller...org:"+org+",Account:"+rechargeReq.getAccount()+",ProductNo:"+rechargeReq.getProductNo()+",OrgOrderNo:"+rechargeReq.getOrgOrderNo());
        BaseResponse<RechargeRes> res = new BaseResponse<RechargeRes>();
        try {
            //step1.根据服务产品编号查询对应的服务产品
            log.info("根据产品编号查询充值产品,ProductNo："+rechargeReq.getProductNo());
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(rechargeReq.getProductNo());
            if (productInfo == null || !ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())){
                log.info("根据服务产品编号查询对应的服务产品失败,ProductNo:"+rechargeReq.getProductNo());
                res.setResponseCode(productInfo.getResponseCode());
                res.setResponseMsg(productInfo.getResponseMsg());
                return res;
            }

            //step2.创建充值订单
            CreateRechargeOrderVo createRechargeOrderVo = new CreateRechargeOrderVo();
            createRechargeOrderVo.setOrderDesc(rechargeReq.getOrderDesc());
            createRechargeOrderVo.setOrgNo(org);
            createRechargeOrderVo.setOrgOrderNo(rechargeReq.getOrgOrderNo());
            createRechargeOrderVo.setProductNo(rechargeReq.getProductNo());
            createRechargeOrderVo.setAccountNumber(rechargeReq.getAccount());
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData().getOrderInfoVo()){
                log.error("创建订单失败");
                res.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                res.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return res;
            }
            log.info("创建订单成功,订单编号:"+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());

            //step3.调用互亿话费充值接口进行话费充值
            log.info("调用互亿工具类进行话费充值");
            IhuyiRechargeRes ihuyiRechargeRes = IhuyiUtils.ihuyiPhoneRecharge(rechargeOrderRes.getData(),rechargeReq,configFeignClient);
            RechargeRes rechargeRes = new RechargeRes();
            //1:提交成功； 0、1015、1016、4001：核单处理,订购成功
            if (1 == ihuyiRechargeRes.getCode() || 0 == ihuyiRechargeRes.getCode() || 1015 == ihuyiRechargeRes.getCode()
                    || 1016 == ihuyiRechargeRes.getCode() || 4001 == ihuyiRechargeRes.getCode()) {
                log.info("互亿话费充值下单成功,互亿充值结果状态码:"+ihuyiRechargeRes.getCode());
                rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_HANDING);    //订单状态:处理中
                rechargeOrderRes.getData().getOrderInfoVo().setNotifyUrl(rechargeReq.getNotifyUrl());               //充值结果通知地址(机构接收地址)
                if (!StringUtils.isEmpty(ihuyiRechargeRes.getTaskid())) {
                    rechargeOrderRes.getData().getOrderInfoVo().setSupplierOrderNo(ihuyiRechargeRes.getTaskid());   //商户订单号
                }

                //调用订单服务更新订单
                log.info("互亿话费充值下单成功更新订单,订单编号:"+rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData() || null == rechargeOrderRes.getData().getOrderInfoVo()) {
                    res.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return res;
                }

                //互亿话费充值下单成功响应结果
                res.getData().setRechargeStatus(ModelConstants.RECHARGE_ING);
                res.getData().setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo());
                return res;

            }else {

                //充值订购失败直接响应失败结果,并通知机构
                log.info("互亿话费充值订购失败");
                rechargeOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL); //订单失败
                rechargeOrderRes.getData().getOrderInfoVo().setOrderFailDesc("互亿话费充值下单失败");

                //调用订单服务更新订单
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData() || null == rechargeOrderRes.getData().getOrderInfoVo()) {
                    res.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    res.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return res;
                }

                //订购失败,通知机构下单失败
                log.info("封装通知请求数据：订单状态失败");
                OrderInfoVo orderInfoVo = new OrderInfoVo();
                orderInfoVo.setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);   //订单状态失败
                orderInfoVo.setOrgOrderNo(rechargeReq.getOrgOrderNo()); //机构订单号
                orderInfoVo.setOrderNo(rechargeOrderRes.getData().getOrderInfoVo().getOrderNo()); //订单号
                orderInfoVo.setNotifyUrl(rechargeOrderRes.getData().getOrderInfoVo().getNotifyUrl());//异步通知地址

                //查询机构信息
                BaseResponse<OrgInfoVo> orgInf = orgFeignClient.findOrgByNo(org);
                if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
                    log.error("互亿话费充值通知,合作机构信息获取失败");
                    res.setResponseCode(ResponseCodeMsg.ILLEGAL_PARTNER.getCode());
                    res.setResponseMsg(ResponseCodeMsg.ILLEGAL_PARTNER.getMsg());
                    return res;
                }

                //通知机构
                log.info("开始通知机构...");
                //TODO 这里调用通知服务,写通知机构的逻辑

                //设置响应结果
                res.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
                res.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
                res.getData().setRechargeStatus(ModelConstants.RECHARGE_FIAL);
                return res;
            }

        }catch(Exception e){
            log.info("互亿话费充值出现异常,异常信息:"+e.getMessage());
            throw new RechargeException(ResponseCodeMsg.IHUYI_PHONE_RECHARGE_EXCEPTION.getCode(),ResponseCodeMsg.IHUYI_PHONE_RECHARGE_EXCEPTION.getMsg());
        }
    }

    /**
     * 互亿话费充值结果推送Controller
     * @param request
     * @param response
     */
    @Override
    public void ihuyiPhoneRechargeCallBack(HttpServletRequest request, HttpServletResponse response){
        log.info("互亿话费充值结果有新的通知消息......");
        response.setContentType("text/html;charset=utf-8");
        String resultStr = "success";
        PrintWriter writer  = null;
        try {
            //step1.从商户请求体中取出数据
            writer = response.getWriter();
            String taskid = request.getParameter("taskid");
            String orderid = request.getParameter("orderid");
            String mobile = request.getParameter("mobile");
            String message = request.getParameter("message");
            String state = request.getParameter("state");
            String sign = request.getParameter("sign");

            //step2.封装参数
            Map<String, String> map = new HashMap();
            map.put("taskid", taskid);
            map.put("mobile", mobile);
            map.put("state", state);   //话费充值
            map.put("message", message);
            String validateSign = IhuyiUtils.getSign(map,configFeignClient);
            map.put("orderid",orderid);
            map.put("sign",sign);
            log.info("互亿话费充值回调，互亿回调请求数据:" + JSONObject.toJSONString(map));
            if (validateSign.equals(sign)) {
                log.info("互亿话费充值回调，验签通过");

                //查询互亿通知本笔订单信息
                BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrderNo(orderid);
                if (null != rechargeOrderVo.getData().getOrderInfoVo() && !ModelConstants.ORDER_MERCHANT_FLAG_IHUYI.equals(rechargeOrderVo.getData().getOrderInfoVo().getSupplierFlag())) {
                    writer.print(resultStr);
                    return;
                }

                //如果商户订单号为空，就添加商户订单号
                if (org.apache.commons.lang.StringUtils.isEmpty(rechargeOrderVo.getData().getOrderInfoVo().getSupplierOrderNo())) {
                    rechargeOrderVo.getData().getOrderInfoVo().setSupplierOrderNo(taskid);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData()); //更新商户订单
                }

                //查询机构信息
                BaseResponse<OrgInfoVo> orgInf = orgFeignClient.findOrgByNo(rechargeOrderVo.getData().getOrderInfoVo().getOrgNo());
                if(!ResponseCodeMsg.SUCCESS.getCode().equals(orgInf.getResponseCode())) {
                    log.error("互亿话费通知,查询机构信息失败");
                    return;
                }

                //充值成功
                if ("1".equals(state)) {
                    log.info("充值成功");
                    if (rechargeOrderVo.getData().getOrderInfoVo() != null && ModelConstants.ORDER_STATUS_HANDING.equals(rechargeOrderVo.getData().getOrderInfoVo().getOrderStatus())) {
                        rechargeOrderVo.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                        orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                        log.info("互亿推送充值结果:互亿话费充值订单更新成功");
                        writer.print(resultStr);

                        //向机构通知成功消息(平台订单号，机构订单号，订单状态，机构信息)
                        log.info("封装通知请求数据：订单状态成功");
                        OrderInfoVo orderInfoVo = new OrderInfoVo();
                        orderInfoVo.setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);   //订单状态成功
                        orderInfoVo.setOrgOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrgOrderNo()); //机构订单号
                        orderInfoVo.setOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrderNo()); //订单号
                        orderInfoVo.setNotifyUrl(rechargeOrderVo.getData().getOrderInfoVo().getNotifyUrl());//异步通知地址

                        //通知机构（订单数据，机构信息）
                        log.info("开始通知机构...");
                        //TODO 这里调用通知服务,写通知机构的逻辑
                        //notifyToOrg(serviceOrder,orgInf.getData());

                    } else if (rechargeOrderVo.getData().getOrderInfoVo() == null){
                        log.info("互亿推送充值结果:互亿话费订购的订单号不存在!");
                        writer.print(resultStr);
                    } else {
                        log.info("互亿推送充值结果:互亿话费订购,该订单状态已处理");
                        writer.print(resultStr);
                    }
                } else { //充值失败
                    log.info("互亿推送充值结果:话费充值失败,订单状态更新为失败");
                    rechargeOrderVo.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                    resultStr = "fail";
                    writer.print(resultStr);

                    //向机构通知失败消息(平台订单号，机构订单号，订单状态，机构信息)
                    log.info("封装通知请求数据：订单状态失败");
                    OrderInfoVo serviceOrder = new OrderInfoVo();
                    serviceOrder.setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);   //订单状态失败
                    serviceOrder.setOrgOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrgOrderNo()); //机构订单号
                    serviceOrder.setOrderNo(rechargeOrderVo.getData().getOrderInfoVo().getOrderNo()); //订单号
                    serviceOrder.setNotifyUrl(rechargeOrderVo.getData().getOrderInfoVo().getNotifyUrl());//异步通知地址

                    //充值失败,通知机构
                    log.info("开始通知机构...");
                    //notifyToOrg(serviceOrder,orgInf.getData());
                    //TODO 通知机构
                }
            } else {
                log.info("互亿推送充值结果:互亿话费充值回调,验签失败");
                resultStr = "fail";
                writer.print(resultStr);
            }
        } catch (Exception e) {
            log.info("互亿话费充值推送出现异常,异常信息:"+e.getMessage());
            resultStr = "fail";
            writer.print(resultStr);
            throw new RechargeException(ResponseCodeMsg.IHUYI_PUSH_PHONE_RECHARGE_RES_EXCEPTION.getCode(),ResponseCodeMsg.IHUYI_PUSH_PHONE_RECHARGE_RES_EXCEPTION.getMsg());
        }
        log.info("互亿推送充值结果:互亿话费充值回调返回字符：" + resultStr);
    }
}
