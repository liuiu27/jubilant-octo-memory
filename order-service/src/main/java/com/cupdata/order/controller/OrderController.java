package com.cupdata.order.controller;


import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.IOrderController;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author junliang
 * @date 2018/04/11
 */
@Slf4j
@RestController
public class OrderController implements IOrderController {


    @Override
    public BaseResponse<VoucherOrderVo> createVoucherOrder(@RequestBody CreateVoucherOrderVo createVoucherOrderVo) {
        log.info("OrderController createVoucherOrder is begin params is" + createVoucherOrderVo.toString());
        try {
            BaseResponse<VoucherOrderVo> voucherOrderRes = new BaseResponse();
            // 根据产品编号,查询服务产品信息
            BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(createVoucherOrderVo.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
                    || null == productInfRes.getData()) {// 如果查询失败
                log.error("product-service findByProductNo is error ProductNo is " + createVoucherOrderVo.getProductNo());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
                return voucherOrderRes;
            }

            //获取商户标识
            BaseResponse<SupplierInfVo>  SupplierInfVo = supplierClient.findSupByNo(productInfRes.getData().getProduct().getSupplierNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(SupplierInfVo.getResponseCode())
                    || null == SupplierInfVo.getData()){
                voucherOrderRes.setResponseCode(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.GET_SUPPLIER_FAIL_BY_NO.getMsg());
                return voucherOrderRes;
            }
            String supplierFlag = SupplierInfVo.getData().getSuppliersInf().getSupplierFlag();

            // 查询机构、商品关系记录
            BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(createVoucherOrderVo.getOrgNo(),
                    createVoucherOrderVo.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                    || null == orgProductRelRes.getData()) {// 如果查询失败
                log.error("product-service findRel is error ProductNo is " + createVoucherOrderVo.getProductNo()
                        + "orgNo is" + createVoucherOrderVo.getOrgNo());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
                return voucherOrderRes;
            }

            ServiceOrderVoucher voucherOrder = orderBiz.createVoucherOrder(supplierFlag , createVoucherOrderVo.getOrgNo(),
                    createVoucherOrderVo.getOrgOrderNo(), createVoucherOrderVo.getOrderDesc(),
                    productInfRes.getData().getProduct(), orgProductRelRes.getData().getOrgProductRela());
            if (null == voucherOrder) {// 创建券码订单失败
                log.error("order-service createVoucherOrder is error findRel is " + createVoucherOrderVo.getProductNo()
                        + "orgNo is" + createVoucherOrderVo.getOrgNo());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return voucherOrderRes;
            }
            ServiceOrder order = orderBiz.select(Integer.parseInt(voucherOrder.getOrderId().toString()));
            if (null == order) {
                log.error("order-service select is error id is" + voucherOrder.getOrderId());
                voucherOrderRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                voucherOrderRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return voucherOrderRes;
            }
            VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
            voucherOrderVo.setOrder(order);
            voucherOrderVo.setVoucherOrder(voucherOrder);
            voucherOrderRes.setData(voucherOrderVo);
            return voucherOrderRes;
        } catch (Exception e) {
            log.error("error is " + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }

        return null;
    }

    @Override
    public BaseResponse<VoucherOrderVo> updateVoucherOrder(VoucherOrderVo voucherOrderVo) {
        return null;
    }

    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
        return null;
    }

    @Override
    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
        return null;
    }

    @Override
    public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(String sup, String supplierOrderNo, String voucherCode) {
        return null;
    }

    @Override
    public BaseResponse<RechargeOrderVo> createRechargeOrder(CreateRechargeOrderVo createRechargeOrderVo) {
        return null;
    }

    @Override
    public BaseResponse<RechargeOrderVo> updateRechargeOrder(RechargeOrderVo rechargeOrderVo) {
        return null;
    }

    @Override
    public Integer updateServiceOrder(OrderInfoVo order) {
        return null;
    }

    @Override
    public BaseResponse<List<OrderInfoVo>> getServiceOrderListByParam(Character orderStatus, String orderSubType, String supplierFlag) {
        return null;
    }

    @Override
    public List<OrderInfoVo> selectMainOrderList(Character orderStatus, String supplierFlag, String orderSubType) {
        return null;
    }

    @Override
    public BaseResponse queryContentOrder() {
        return null;
    }

    @Override
    public BaseResponse createContentOrder() {
        return null;
    }

    @Override
    public BaseResponse updateContentOrder() {
        return null;
    }
}
