package com.cupdata.order.biz;

import java.util.HashMap;
import java.util.Map;

import com.cupdata.commons.model.*;
import com.cupdata.commons.vo.product.RechargeOrderVo;
import com.cupdata.order.dao.ServiceOrderRechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.order.dao.ServiceOrderDao;
import com.cupdata.order.dao.ServiceOrderVoucherDao;
import com.cupdata.order.util.OrderUtils;

/**
 * @Auth: LinYong
 * @Description: 订单业务
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceOrderBiz extends BaseBiz<ServiceOrder> {
    @Autowired
    private ServiceOrderDao orderDao;

    @Autowired
    private ServiceOrderVoucherDao orderVoucherDao;

    @Autowired
	private ServiceOrderRechargeDao orderRechargeDao;

    @Override
    public BaseDao<ServiceOrder> getBaseDao() {
        return orderDao;
    }

    /**
     * 根据券码产品，创建券码订单
     * @param voucherProduct 券码商品
     * @return
     */

    //创建券码订单
    public ServiceOrderVoucher createVoucherOrder(String orgNo, String orgOrderNo, String orderDesc, ServiceProduct voucherProduct, OrgProductRela orgProductRela){
        //初始化主订单记录
        ServiceOrder order = OrderUtils.initServiceOrder(orgNo, orgOrderNo, orderDesc, voucherProduct, orgProductRela);
        orderDao.insert(order);//插入主订单

        //初始化券码订单
        ServiceOrderVoucher voucherOrder = OrderUtils.initVoucherOrder(order, voucherProduct.getProductNo());
        orderVoucherDao.insert(voucherOrder);//插入券码订单
        return voucherOrder;
    }

    //更新券码订单
	public void updateVoucherOrder(VoucherOrderVo voucherOrderVo) {
		//修改订单表状态
		orderDao.update(voucherOrderVo.getOrder());
		
		//修改券码表 券码号
		orderVoucherDao.update(voucherOrderVo.getVoucherOrder());
	}

	//创建充值订单
	public ServiceOrderRecharge createRechargeOrder(String orgNo, String orgOrderNo, String orderDesc, ServiceProduct recharge, OrgProductRela orgProductRela){
		//初始化主订单记录
		ServiceOrder order = OrderUtils.initServiceOrder(orgNo, orgOrderNo, orderDesc, recharge, orgProductRela);
		orderDao.insert(order);//插入主订单

		//初始化充值订单
        ServiceOrderRecharge rechargeOrder = OrderUtils.initRechargeOrder(order,recharge.getProductNo());
        orderRechargeDao.insert(rechargeOrder);
        return rechargeOrder;
	}

	//更新充值订单
    public void updateRechargeOrder(RechargeOrderVo rechargeOrderVo){
        //修改订单表状态
        orderDao.update(rechargeOrderVo.getOrder());
        //修改充值订单表
        orderRechargeDao.update(rechargeOrderVo.getRechargeOrder());
    }


	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
		BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("orgNo", orgNo);
		paramMap.put("orgOrderNo", orgOrderNo);
    	ServiceOrder order = orderDao.selectSingle(paramMap);
    	if(null == order) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
    	paramMap.clear();
    	paramMap.put("orderId", order.getId());
    	ServiceOrderVoucher voucherOrder =  orderVoucherDao.selectSingle(paramMap);
    	if(null == voucherOrder) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
    	voucherOrderVo.setOrder(order);
    	voucherOrderVo.setVoucherOrder(voucherOrder);
    	res.setData(voucherOrderVo);
    	return res;
	}

	public BaseResponse<VoucherOrderVo> getVoucherOrderByVoucher(String sup,String supplierOrderNo,String voucherCode) {
		BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("voucherCode", voucherCode);
		ServiceOrderVoucher voucherOrder =  orderVoucherDao.selectSingle(paramMap);
		if(null == voucherOrder) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
		paramMap.clear();
		paramMap.put("id", voucherOrder.getOrderId());
    	ServiceOrder order = orderDao.selectSingle(paramMap);
    	if(null == order) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
    	voucherOrderVo.setOrder(order);
    	voucherOrderVo.setVoucherOrder(voucherOrder);
    	res.setData(voucherOrderVo);
    	return res;
	}

	public BaseResponse<VoucherOrderVo> getVoucherOrderByOrderNo(String orderNo) {
		BaseResponse<VoucherOrderVo> res = new BaseResponse<>();
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("orderNo", orderNo);
    	ServiceOrder order = orderDao.selectSingle(paramMap);
    	if(null == order) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
    	paramMap.clear();
    	paramMap.put("orderId", order.getId());
    	ServiceOrderVoucher voucherOrder =  orderVoucherDao.selectSingle(paramMap);
    	if(null == voucherOrder) {
    		res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
			res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
			return res;
    	}
    	voucherOrderVo.setOrder(order);
    	voucherOrderVo.setVoucherOrder(voucherOrder);
    	res.setData(voucherOrderVo);
    	return res;
	}
}
