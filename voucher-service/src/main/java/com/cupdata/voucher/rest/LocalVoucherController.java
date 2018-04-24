package com.cupdata.voucher.rest;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.ILocalVoucherController;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.*;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.VoucherException;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.voucher.biz.VoucherBiz;
import com.cupdata.voucher.feign.OrderFeignClient;
import com.cupdata.voucher.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 从本地券码库获取券码
 * @@Date: Created in 10:53 2018/4/13
 */
@Slf4j
@RestController
public class LocalVoucherController implements ILocalVoucherController {

    @Autowired
    private ProductFeignClient productFeignClient ;

    @Autowired
    private VoucherBiz voucherBiz;

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * SIP从本地券码库获取券码,进行券码激活
     * @param org
     * @param voucherReq
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getLocalVoucher(@RequestParam("org") String org, @RequestBody GetVoucherReq voucherReq) {
        log.info("调用SIP从本地获取券码Controller......org:"+org+",ProductNo"+voucherReq.getProductNo());
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<>();
        try {
            //step1.获取产品信息,如果不存在此产品,直接返回错误状态码和信息
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                log.info("VoucherController getLocalVoucher 获取产品失败");
                getVoucherRes.setResponseCode(productInfo.getResponseCode());
                getVoucherRes.setResponseMsg(productInfo.getResponseMsg());
                return getVoucherRes;
            }

            //step2.根据产品编号查询商品类型(供应商参数)
            Long categoryId = Long.valueOf(productInfo.getData().getSupplierParam());

            //step3.根据券码类型获得券码状态是否有效
            ElectronicVoucherCategory electronicVoucherCategory = voucherBiz.checkVoucherValidStatusByCategoryId(categoryId);

            //step4.判断券码列表是否存在有效券码,获取有效券码
            ElectronicVoucherLib electronicVoucherLib = voucherBiz.selectVoucherByCategoryId(electronicVoucherCategory.getId());

            //step5.券码列表存在可用券码,从券码中获取券码号，并更新
            electronicVoucherLib.setOrgOrderNo(voucherReq.getOrgOrderNo());
            electronicVoucherLib.setOrgNo(org);
            electronicVoucherLib.setMobileNo(voucherReq.getMobileNo());
            voucherBiz.UpdateElectronicVoucherLib(electronicVoucherLib);

            //step6.封装券码信息,给予返回
            log.info("本地获取券码成功,券码号:"+electronicVoucherLib.getTicketNo());
            GetVoucherRes res = new GetVoucherRes();
            res.setVoucherLibId(electronicVoucherLib.getId());      //该条券码id
            res.setVoucherCode(electronicVoucherLib.getTicketNo()); //券码号
            res.setStartDate(electronicVoucherLib.getStartDate());  //开始时间
            res.setExpire(electronicVoucherLib.getEndDate());       //结束时间
            getVoucherRes.setData(res);
            return getVoucherRes;
        }catch (Exception e){
            log.error("LocalVoucherController getLocalVoucher error is:" + e.getMessage());
            getVoucherRes.setResponseMsg(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getMsg());
            getVoucherRes.setResponseCode(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getCode());
            return getVoucherRes;
        }
    }

    /**
     * 其他机构通过SIP来获取券码号(需要创建订单)
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam("org") String org, @RequestBody GetVoucherReq voucherReq) {
        log.info("调用ORG通过SIP获取券码Controller......org:"+org+",ProductNo"+voucherReq.getProductNo());
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse();
        try {
            //step1.获取产品信息,如果不存在此产品,直接返回错误状态码和信息
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                log.info("LocalVoucherController getVoucher 获取产品失败");
                getVoucherRes.setResponseCode(productInfo.getResponseCode());
                getVoucherRes.setResponseMsg(productInfo.getResponseMsg());
                return getVoucherRes;
            }

            //step2.创建券码订单
            CreateVoucherOrderVo createvoucherOrderVo = new CreateVoucherOrderVo();
            createvoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
            createvoucherOrderVo.setOrgNo(org);
            createvoucherOrderVo.setProductNo(voucherReq.getProductNo());
            createvoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
            log.info("创建券码订单,ProductNo:"+voucherReq.getProductNo()+",org"+org);
            BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createvoucherOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                    || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrderInfoVo()) {
                log.info("创建订单失败！");
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //step3.根据产品编号查询商品类型(供应商参数)
            Long categoryId = Long.valueOf(productInfo.getData().getSupplierParam());

            //step4.根据券码类型获得券码状态是否有效
            ElectronicVoucherCategory electronicVoucherCategory = voucherBiz.checkVoucherValidStatusByCategoryId(categoryId);

            //step5.判断券码列表是否存在有效券码,获取有效券码
            ElectronicVoucherLib electronicVoucherLib = voucherBiz.selectVoucherByCategoryId(categoryId);

            //step6.券码列表存在可用券码,从券码中获取券码号，并更新
            electronicVoucherLib.setOrgOrderNo(voucherReq.getOrgOrderNo());
            electronicVoucherLib.setOrgNo(org);
            electronicVoucherLib.setMobileNo(voucherReq.getMobileNo());
            voucherBiz.UpdateElectronicVoucherLib(electronicVoucherLib);

            //step7.获取券码成功，修改订单保存券码状态
            voucherOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
            voucherOrderRes.getData().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
            voucherOrderRes.getData().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
            voucherOrderRes.getData().getOrderInfoVo().setSupplierOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
            voucherOrderRes.getData().setVoucherCode(electronicVoucherLib.getTicketNo());  //券码号
            voucherOrderRes.getData().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd")); //起始时间
            voucherOrderRes.getData().setEndDate(electronicVoucherLib.getEndDate());

            //step8.调用订单服务更新订单
            BaseResponse baseResponse = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(baseResponse.getResponseCode())
                    || null == voucherOrderRes.getData() || null == voucherOrderRes.getData().getOrderInfoVo()) {
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //step9.响应结果
            log.info("org获取sip券码结果为:券码号"+electronicVoucherLib.getTicketNo()+",有效期:"+electronicVoucherLib.getEndDate());
            GetVoucherRes res = new GetVoucherRes();
            res.setOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());      //平台订单号
            res.setOrgOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrgOrderNo());//机构订单编号
            res.setVoucherCode(electronicVoucherLib.getTicketNo()); //券码号
            res.setStartDate(electronicVoucherLib.getStartDate());  //开始时间
            res.setExpire(electronicVoucherLib.getEndDate());       //结束时间
            getVoucherRes.setData(res);
            return getVoucherRes;
        }catch (Exception e){
            log.error("LocalVoucherController getLocalVoucher error is:" + e.getMessage());
            getVoucherRes.setResponseMsg(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getMsg());
            getVoucherRes.setResponseCode(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getCode());
            return getVoucherRes;
        }
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
