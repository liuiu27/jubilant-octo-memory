package com.cupdata.ihuyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.api.ihuyi.IHuyiPhoneController;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 互亿话费充值业务
 * @CreateDate: 2018/3/6 15:59
 */
@Slf4j
@RestController
public class IhuyiPhoneRechargeController implements IHuyiPhoneController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    @Autowired
    private  CacheFeignClient cacheFeignClient ;

    /**
     * 互亿话费充值
     * @param org
     * @param rechargeReq
     * @param request
     * @param response
     * @return
     */
    @Override
    public BaseResponse<RechargeRes> recharge(@RequestParam(value="org", required=true) String org,@RequestBody RechargeReq rechargeReq, HttpServletRequest request, HttpServletResponse response) {

        log.info("进入互亿话费充值controller......Account:"+rechargeReq.getAccount()+",MobileNo:"+rechargeReq.getMobileNo()+",ProductNo:"+rechargeReq.getProductNo()+",OrderDesc:"+rechargeReq.getOrderDesc());
        //设置响应结果
        BaseResponse<RechargeRes> rechargeRes = new BaseResponse<RechargeRes>();
        try {
            //step1.根据服务产品编号查询对应的服务产品
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

            //调用订单服务创建订单
            log.info("开始创建互亿话费充值订单...");
            BaseResponse<RechargeOrderVo> rechargeOrderRes = orderFeignClient.createRechargeOrder(createRechargeOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                    || null == rechargeOrderRes.getData()
                    || null == rechargeOrderRes.getData().getOrder()
                    || null == rechargeOrderRes.getData().getRechargeOrder()){
                //创建订单失败，设置响应错误消息和错误状态码，给予返回
                rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return rechargeRes;
            }

            //step3.调用互亿充值工具类进行话费充值
            log.info("开始调用互亿充值工具类进行话费充值...");
            IhuyiRechargeRes ihuyiRechargeRes = IhuyiUtils.ihuyiPhoneRecharge(rechargeOrderRes.getData(),rechargeReq,cacheFeignClient);
            RechargeRes res = new RechargeRes();
            //1:提交成功； 0、1015、1016、4001：核单处理,订购成功
            if (1 == ihuyiRechargeRes.getCode() || 0 == ihuyiRechargeRes.getCode()
                    || 1015 == ihuyiRechargeRes.getCode()
                    || 1016 == ihuyiRechargeRes.getCode()
                    || 4001 == ihuyiRechargeRes.getCode()) {
                log.info("互亿话费充值下单成功,互亿充值结果状态码:"+ihuyiRechargeRes.getCode());
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_HANDING);    //订单状态:处理中
                rechargeOrderRes.getData().getOrder().setNotifyUrl(rechargeReq.getNotifyUrl());               //充值结果通知地址(机构接收地址)
                rechargeOrderRes.getData().getRechargeOrder().setAccountNumber(rechargeReq.getAccount());     //充值账号
                rechargeOrderRes.getData().getRechargeOrder().setRechargeAmt(rechargeReq.getRechargeAmt());      //充值金额
                rechargeOrderRes.getData().getRechargeOrder().setRechargeNumber(rechargeReq.getRechargeNumber());//充值数量
                if (!StringUtils.isEmpty(ihuyiRechargeRes.getTaskid())) {
                    rechargeOrderRes.getData().getOrder().setSupplierOrderNo(ihuyiRechargeRes.getTaskid());      //商户订单号
                }

                //调用订单服务更新订单
                log.info("调用订单服务更新订单中商户订单号,Taskid:"+ihuyiRechargeRes.getTaskid());
                rechargeOrderRes = orderFeignClient.updateRechargeOrder(rechargeOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(rechargeOrderRes.getResponseCode())
                        || null == rechargeOrderRes.getData()
                        || null == rechargeOrderRes.getData().getOrder()
                        || null == rechargeOrderRes.getData().getRechargeOrder()) {
                    rechargeRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    rechargeRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return rechargeRes;
                }

                //互亿话费充值下单成功响应结果
                res.setRechargeStatus(ModelConstants.RECHARGE_ING);
                res.setOrderNo(rechargeOrderRes.getData().getOrder().getOrderNo());
                rechargeRes.setData(res);
                return rechargeRes;
            } else {
                //充值订购失败直接响应失败结果,并通知机构
                log.info("互亿话费充值订购失败,直接通知机构...");
                rechargeOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL); //订单失败
                rechargeOrderRes.getData().getOrder().setOrderFailDesc("互亿话费充值下单失败");

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

                //通知机构充值结果
                notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderRes.getData().getOrder().getOrderNo());

                //设置响应结果
                rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
                rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
                res.setRechargeStatus(ModelConstants.RECHARGE_FIAL);
                rechargeRes.setData(res);
                return rechargeRes;
            }
        }catch (Exception e){

            log.info("互亿话费充值出现异常"+e.getMessage());
            rechargeRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getMsg());
            rechargeRes.setResponseCode(IhuyiRechargeResCode.FAIL_TO_RECHARGE.getCode());
            return rechargeRes;
        }
    }

    /**
     * @Description 互亿话费订购接口回调(此接口用于互亿调用,以此告知SIP话费充值的最终结果)
     * @param @param request
     * @param @param response
     * @param @throws IOException 参数
     * @return void 返回类型
     * @Author KaiZhang
     * @throws
     */
    @RequestMapping(value = "ihuyiPhoneRechargeCallBack", method = {RequestMethod.POST})
    public void ihuyiPhoneRechargeCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        PrintWriter writer = response.getWriter();
        upload.setHeaderEncoding("UTF-8");
        String resultStr = "success";
        List<?> items;
        try {
            items = upload.parseRequest(request);
            Map<String, String> param = new HashMap();
            for(Object object:items){
                FileItem fileItem = (FileItem) object;
                if (fileItem.isFormField()) {
                    param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
                }
            }

            //从商户请求体中取出数据
            String taskid = param.get("taskid");
            String orderid = param.get("orderid");
            String mobile = param.get("mobile");
            String message = param.get("message");
            String state = param.get("state");
            String sign = param.get("sign");

            //封装参数
            Map<String, String> map = new HashMap();
            map.put("taskid", taskid);
            map.put("mobile", mobile);
            map.put("state", state);   //话费充值
            map.put("message", message);
            String validateSign = IhuyiUtils.getSign(map,cacheFeignClient);
            map.put("orderid",orderid);
            map.put("sign",sign);
            log.info("互亿话费充值回调，互亿请求数据:" + JSONObject.toJSONString(map));
            if (validateSign.equals(sign)) {
                log.info("互亿话费充值回调，验签通过");
                BaseResponse<RechargeOrderVo> rechargeOrderVo = orderFeignClient.getRechargeOrderByOrderNo(orderid);
                if (null != rechargeOrderVo.getData().getOrder() && !ModelConstants.ORDER_MERCHANT_FLAG_IHUYI.equals(rechargeOrderVo.getData().getOrder().getSupplierFlag())) {
                    writer.print(resultStr);
                    return;
                }

                //如果商户订单号为空，就添加商户订单号
                if (org.apache.commons.lang.StringUtils.isEmpty(rechargeOrderVo.getData().getOrder().getSupplierNo())) {
                    rechargeOrderVo.getData().getOrder().setSupplierOrderNo(taskid);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData()); //更新商户订单
                }

                if ("1".equals(state)) {  //充值成功
                    rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                    if (rechargeOrderVo.getData().getOrder() != null && ModelConstants.ORDER_STATUS_HANDING.equals(rechargeOrderVo.getData().getOrder().getOrderStatus())) {
                        orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                        log.info("互亿推送充值结果...互亿话费充值订单更新成功");
                        writer.print(resultStr);
                        //向机构通知订购成功消息
                        notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
                    } else if (rechargeOrderVo.getData().getOrder() == null){
                        log.info("互亿推送充值结果...互亿话费订购状态的订单号不存在");
                        writer.print(resultStr);
                    } else {
                        log.info("互亿推送充值结果...互亿话费订购,状态已处理");
                        writer.print(resultStr);
                    }
                } else { //充值失败
                    log.info("互亿推送充值结果...话费充值失败,订单状态更新为失败");
                    rechargeOrderVo.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);
                    orderFeignClient.updateRechargeOrder(rechargeOrderVo.getData());//更新订单状态
                    resultStr = "fail";
                    writer.print(resultStr);
                    //充值失败,通知机构
                    notifyFeignClient.rechargeNotifyToOrg3Times(rechargeOrderVo.getData().getOrder().getOrderNo());
                }
            } else {
                log.info("互亿推送充值结果...互亿话费充值回调，验签失败");
                resultStr = "fail";
                writer.print(resultStr);
            }
        } catch (FileUploadException e) {
            log.info("",e);
            resultStr = "fail";
            writer.print(resultStr);
        }
        log.info("互亿推送充值结果...互亿话费充值回调返回字符：" + resultStr);
    }
}
