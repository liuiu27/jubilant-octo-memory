package com.cupdata.lakala.rest;

import com.cupdata.lakala.feign.ConfigFeignClient;
import com.cupdata.lakala.feign.OrderFeignClient;
import com.cupdata.lakala.feign.ProductFeignClient;
import com.cupdata.lakala.utils.LakalaVoucherUtil;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.lakala.ILakalaController;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.LakalaVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 拉卡拉获取券码相关Controller
 * @@Date: Created in 10:51 2018/4/13
 */
@Slf4j
@RestController
public class LakalaController implements ILakalaController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient;

    /**
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam("org") String org, @RequestBody GetVoucherReq voucherReq) {
        log.info("lakala获取券码controller,OrgorderNo:" + voucherReq.getOrgOrderNo() + ",OrderDesc:" + voucherReq.getOrderDesc() + ",org:" + org + ",mobileNo:" + voucherReq.getMobileNo()+",ProductNo"+voucherReq.getProductNo());
        //设置响应数据结果
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<GetVoucherRes>();
        try {
            //获取该供应商产品,如果不存在此产品,直接返回错误状态码和信息
            log.info("lakala获取券码controller开始获取供应商产品");
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
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
                    || null == voucherOrderRes.getData().getOrderInfoVo()) {   //如果创建订单失败
                //设置错误状态码和错误消息,给予返回
                log.info("lakala controller : 创建订单失败");
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return getVoucherRes;
            }
            //调用拉卡拉券码工具类获取券码,从请求体中获取需要传递的参数：手机号，订单号，业务参数，订单描述
            LakalaVoucherRes lakalaVoucherRes = LakalaVoucherUtil.obtainvValidTicketNo(voucherReq.getMobileNo(),voucherOrderRes.getData().getOrderInfoVo().getOrderNo(),productInfo.getData().getSupplierParam(),voucherReq.getOrderDesc(),configFeignClient);
            //对返回数据进行异常处理
            if (null == lakalaVoucherRes || !lakalaVoucherRes.getRes()){
                log.info("lakala获取券码controller:券码获取失败");
                getVoucherRes.setResponseCode(ResponseCodeMsg.FAILED_TO_GET.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.FAILED_TO_GET.getMsg());
                return getVoucherRes;
            }

            //获取券码成功，修改订单保存券码状态
            voucherOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
            voucherOrderRes.getData().getOrderInfoVo().setSupplierOrderNo(lakalaVoucherRes.getData().getOrder_id());      //主订单中存入供应商订单编号
            voucherOrderRes.getData().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
            voucherOrderRes.getData().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
            voucherOrderRes.getData().setVoucherCode(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_num()); //券码号
            voucherOrderRes.getData().setVoucherPassword(lakalaVoucherRes.getData().getVoucherList().get(0).getVoucher_pass());    //卡密
            voucherOrderRes.getData().setQrCodeUrl(lakalaVoucherRes.getData().getVoucherList().get(0).getUrl());                   //二维码短链接
            voucherOrderRes.getData().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd")); //起始时间
            voucherOrderRes.getData().setEndDate(lakalaVoucherRes.getData().getVoucherList().get(0).getEnd_time());                //有效期结束时间

            //调用订单服务更新订单
            log.info("lakala获取券码controller更新券码订单");
            BaseResponse baseResponse = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())
                    || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrderInfoVo()) {
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
            voucherRes.setOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
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

    /**
     * @param org 机构编号
     * @param disableVoucherReq 禁用券码请求参数（实现方法需要添加@RequestBody注解获取参数）
     * @return
     */
    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
        return null;
    }

    /**
     * @param sup 商户编号
     * @param writeOffVoucherReq 核销券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
