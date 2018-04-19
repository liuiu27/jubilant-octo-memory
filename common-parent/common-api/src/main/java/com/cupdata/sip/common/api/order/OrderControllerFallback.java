package com.cupdata.sip.common.api.order;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;

import java.util.List;

/**
 * @author junliang
 * @date 2018/04/11
 */
public class OrderControllerFallback implements IOrderController {
    @Override
    public BaseResponse<VoucherOrderVo> createVoucherOrder(CreateVoucherOrderVo createVoucherOrderVo) {
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
    public BaseResponse updateServiceOrder(OrderInfoVo order) {
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
