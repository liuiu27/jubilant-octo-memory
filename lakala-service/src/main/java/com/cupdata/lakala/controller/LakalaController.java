package com.cupdata.lakala.controller;

import com.cupdata.commons.api.lakala.ILakalaController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.*;
import com.cupdata.lakala.feign.CacheFeignClient;
import com.cupdata.lakala.feign.OrderFeignClient;
import com.cupdata.lakala.feign.ProductFeignClient;
import com.cupdata.lakala.utils.LakalaVoucherUtil;
import com.cupdata.lakala.vo.LakalaVoucherRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LinYong
 * @Description: 拉卡拉相关的服务
 * @create 2018-01-04 18:29
 */
@Slf4j
@RestController
public class LakalaController implements ILakalaController{

    @Autowired
    private ProductFeignClient productFeignClient ;

    @Autowired
    private CacheFeignClient cacheFeignClient ;

    @Autowired
    private OrderFeignClient orderFeignClient;


    /*
     *获取拉卡拉券码controller
     *本接口需要商户编号org和请求参数voucherReq
     */
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam(value="org", required=true) String org, @RequestBody GetVoucherReq voucherReq) {

        log.info("lakala获取券码controller,OrgorderNo:" + voucherReq.getOrgOrderNo() + ",OrderDesc:" + voucherReq.getOrderDesc() + ",org:" + org + ",mobileNo:" + voucherReq.getMobileNo()+",ProductNo"+voucherReq.getProductNo());
        //设置响应数据结果
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<GetVoucherRes>();
        try {
            //获取该供应商产品,如果不存在此产品,直接返回错误状态码和信息
            log.info("lakala获取券码controller开始获取供应商产品");
            BaseResponse<ProductInfVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                //设置状态码和错误信息,给予返回
                getVoucherRes.setResponseCode(ResponseCodeMsg.QUERY_PRODUCT_INF_NULL.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.QUERY_PRODUCT_INF_NULL.getMsg());
                return getVoucherRes;
            }

            //创建券码订单
            log.info("lakala获取券码controller创建券码订单");
            CreateVoucherOrderVo createvoucherOrderVo = new CreateVoucherOrderVo();
            createvoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
            createvoucherOrderVo.setOrgNo(org);
            createvoucherOrderVo.setProductNo(voucherReq.getProductNo());
            createvoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
            //调用创建订单服务
            BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createvoucherOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                    || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrder()
                    || null == voucherOrderRes.getData().getVoucherOrder()) {   //如果创建订单失败
                //设置错误状态码和错误消息,给予返回
                log.info("lakala controller : 创建订单失败");
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //调用拉卡拉券码工具类获取券码,从请求体中获取需要传递的参数：手机号，订单号，业务参数，订单描述
            LakalaVoucherRes lakalaVoucherRes = LakalaVoucherUtil.obtainvValidTicketNo(voucherReq.getMobileNo(),voucherOrderRes.getData().getOrder().getOrderNo(),productInfo.getData().getProduct().getSupplierParam(),voucherReq.getOrderDesc(),cacheFeignClient);

            //对返回数据进行异常处理
            if (null == lakalaVoucherRes || !lakalaVoucherRes.getRes()){
                log.info("lakala获取券码controller:券码获取失败");

                //修改订单状态
                voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);
                voucherOrderRes.getData().getOrder().setOrderFailDesc("拉卡拉获取券码失败");

                //调用订单服务更新订单
                log.info("lakala获取券码controller更新券码订单");
                voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                        || null == voucherOrderRes.getData()
                        || null == voucherOrderRes.getData().getOrder()
                        || null == voucherOrderRes.getData().getVoucherOrder()) {
                    getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return getVoucherRes;
                }

                //响应用户
                getVoucherRes.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
                return getVoucherRes;
            }

            //获取券码成功，修改订单保存券码状态
            voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
            voucherOrderRes.getData().getOrder().setSupplierOrderNo(lakalaVoucherRes.getData().getOrder_id());      //主订单中存入供应商订单编号
            voucherOrderRes.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
            voucherOrderRes.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
            voucherOrderRes.getData().getVoucherOrder().setVoucherCode(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_num()); //券码号
            voucherOrderRes.getData().getVoucherOrder().setVoucherPassword(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_pass());    //卡密
            voucherOrderRes.getData().getVoucherOrder().setQrCodeUrl(lakalaVoucherRes.getData().getVoucherList().get(0).getUrl());                   //二维码短链接
            voucherOrderRes.getData().getVoucherOrder().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd")); //起始时间
            voucherOrderRes.getData().getVoucherOrder().setEndDate(lakalaVoucherRes.getData().getVoucherList().get(0).getEnd_time());                //有效期结束时间

            //调用订单服务更新订单
            log.info("lakala获取券码controller更新券码订单");
            voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                    || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrder()
                    || null == voucherOrderRes.getData().getVoucherOrder()) {
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //响应参数:响应券码的券码号，卡密，二维码链接，有效期，平台订单号，机构订单唯一标识
            log.info("lakala获取券码controller响应券码数据:券码号："+lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_num());
            GetVoucherRes voucherRes = new GetVoucherRes();
            voucherRes.setVoucherCode(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_num());
            voucherRes.setVoucherPassword(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_pass());
            voucherRes.setQrCodeUrl(lakalaVoucherRes.getData().getVoucherList().get(0).getUrl());
            voucherRes.setExpire(voucherReq.getExpire());
            voucherRes.setOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());
            voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
            getVoucherRes.setData(voucherRes);
            return getVoucherRes;
        } catch (Exception e) {
                log.error("lakala controller getVoucher error is" + e.getMessage());
                getVoucherRes.setResponseCode(ResponseCodeMsg.SYSTEM_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.SYSTEM_ERROR.getMsg());
                return getVoucherRes;
        }
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value="org", required=true) String org, @RequestBody DisableVoucherReq disableVoucherReq) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(@RequestParam(value="sup", required=true) String sup, @RequestBody WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
