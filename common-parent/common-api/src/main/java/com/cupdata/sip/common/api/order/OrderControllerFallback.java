package com.cupdata.sip.common.api.order;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.order.request.CreateContentOrderVo;
import com.cupdata.sip.common.api.order.request.CreateRechargeOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderContentVo;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse updateVoucherOrder(VoucherOrderVo voucherOrderVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(String sup, String supplierOrderNo,
			String voucherCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<RechargeOrderVo> createRechargeOrder(CreateRechargeOrderVo createRechargeOrderVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse updateRechargeOrder(RechargeOrderVo rechargeOrderVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse updateServiceOrder(OrderInfoVo order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<List<OrderInfoVo>> selectMainOrderList(String orderStatus, String supplierFlag,
			String orderSubType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse<OrderContentVo> queryContentOrder(String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse createContentOrder(CreateContentOrderVo createContentOrderVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResponse updateContentOrder(OrderContentVo orderContentVo) {
		// TODO Auto-generated method stub
		return null;
	}
   
}
