package com.cupdata.order.biz;

import com.cupdata.order.util.OrderUtils;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.entity.ServiceOrderVoucher;
import com.cupdata.sip.common.dao.mapper.ServiceOrderMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderRechargeMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderVoucherMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Auth: LinYong
 * @Description: 订单业务
 * @Date: 20:20 2017/12/14
 */
@Slf4j
@Service
public class ServiceOrderBiz {

    @Autowired
    private ServiceOrderMapper orderDao;

    @Autowired
    private ServiceOrderVoucherMapper orderVoucherDao;

    @Autowired
	private ServiceOrderRechargeMapper orderRechargeDao;


    /**
     * 创建券码订单
     * @param supplierFlag
     * @param orgNo
     * @return
     */
    @Transactional
    public VoucherOrderVo createVoucherOrder(String supplierFlag, String orgNo, String orgOrderNo, String orderDesc, ProductInfoVo voucherProduct, OrgProductRelVo orgProductRela){
        log.info("创建券码订单,supplierFlag:"+supplierFlag+",orgNo:"+orgNo+",orderDesc:"+orderDesc);
        VoucherOrderVo voucherOrderVo =new VoucherOrderVo();
        //初始化主订单记录
        ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag ,orgNo, orgOrderNo, orderDesc, voucherProduct, orgProductRela);
        orderDao.insert(order);//插入主订单

        //初始化券码订单
        ServiceOrderVoucher voucherOrder = OrderUtils.initVoucherOrder(order, voucherProduct.getProductNo());
        orderVoucherDao.insert(voucherOrder);//插入券码订单

        BeanCopierUtils.copyProperties(order,voucherOrderVo.getOrderInfoVo());
        BeanCopierUtils.copyProperties(voucherOrder,voucherOrderVo);

        return voucherOrderVo;
    }

    /**
     * 根据订单状态、订单子类型列表、商户标识查询服务订单列表
     * @param orderStatus
     * @param supplierFlag
     * @param orderSubType
     * @return
     */
  public List<OrderInfoVo> selectMainOrderList(Character orderStatus, String supplierFlag, List<String> orderSubType) {
        log.info("根据订单状态,订单子类型,商户标识查询订单列表,supplierFlag:"+supplierFlag);
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderStatus", orderStatus);
        paramMap.put("supplierFlag", supplierFlag);
        paramMap.put("orderSubType", orderSubType);
        List<ServiceOrder> orderList = orderDao.selectMainOrderList(paramMap);
        List<OrderInfoVo> orderInfoVos = new ArrayList<>(orderList.size());
        orderList.forEach(list ->{
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            BeanCopierUtils.copyProperties(list,orderInfoVo);
                });
        return orderInfoVos;

    }

    /**
     * 根据订单状态、订单子类型、商户标识查询服务订单列表
     * @param orderSubType
     * @return
     */
  /*  public List<ServiceOrder> getServiceOrderListByParam(Character orderStatus, String orderSubType, String supplierFlag){
        BaseResponse<ServiceOrderList> res = new BaseResponse<ServiceOrderList>();
        ServiceOrderList serviceOrderListorderList = new ServiceOrderList();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderStatus", orderStatus);
        paramMap.put("orderSubType", orderSubType);
        paramMap.put("supplierFlag", supplierFlag);
        List<ServiceOrder> orderList = orderDao.selectAll(paramMap);
        if (CommonUtils.isNullOrEmptyOfObj(orderList)){
            res.setResponseMsg(ResponseCodeMsg.QUERY_ORDER_EXCEPTION.getMsg());
            res.setResponseCode(ResponseCodeMsg.QUERY_ORDER_EXCEPTION.getCode());
            return res;
        }
        serviceOrderListorderList.setServiceOrderList(orderList);
        res.setData(serviceOrderListorderList);
        return res ;
    }




    *//**
     * 创建内容引入订单
     * @return
     *//*
	public ServiceOrderContent createContentOrder(String supplierFlag ,CreateContentOrderVo createContentOrderVo,ServiceProduct contentProduct, OrgProductRela orgProductRela) {
		//初始化主订单记录
        ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag,createContentOrderVo.getOrgNo(), createContentOrderVo.getOrgOrderNo(), createContentOrderVo.getOrderDesc(), contentProduct, orgProductRela);
        orderDao.insert(order);//插入主订单

        //初始化内容引入订单
        ServiceOrderContent contentOrder = OrderUtils.initContentOrder(order,createContentOrderVo);
       // orderVoucherDao.insert(contentOrder);//插入券码订单
        return contentOrder;
	}

    //更新券码订单
	public void updateVoucherOrder(VoucherOrderVo voucherOrderVo) {
		//修改订单表状态
		orderDao.update(voucherOrderVo.getOrder());
		
		//修改券码表 券码号
		orderVoucherDao.update(voucherOrderVo.getVoucherOrder());
	}

	//创建充值订单
	public ServiceOrderRecharge createRechargeOrder(String accountNumber,ProductInfVo productInfVo, String supplierFlag, String orgNo, String orgOrderNo, String orderDesc, ServiceProduct recharge, OrgProductRela orgProductRela){

        //初始化主订单记录
		ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag ,orgNo, orgOrderNo, orderDesc, recharge, orgProductRela);
		orderDao.insert(order);//插入主订单

		//初始化充值订单
        ServiceOrderRecharge rechargeOrder = OrderUtils.initRechargeOrder(accountNumber,productInfVo,order,recharge.getProductNo());
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


	public VoucherOrderVo getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
        VoucherOrderVo voucherOrderVo = new VoucherOrderVo();

        ServiceOrder serviceOrder =new ServiceOrder();
        serviceOrder.setOrgNo(orgNo);
        serviceOrder.setOrgOrderNo(orgOrderNo);

        serviceOrder = orderDao.selectOne(serviceOrder);

    	paramMap.clear();
    	paramMap.put("orderId", order.getId());
    	ServiceOrderVoucher voucherOrder =  new ServiceOrderVoucher();
        voucherOrder.setOrderId(serviceOrder.getId());

                orderVoucherDao.selectSingle(paramMap);
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

	public BaseResponse<RechargeOrderVo> getRechargeOrderByOrgNoAndOrgOrderNo(String orgNo , String orgOrderNo){
        BaseResponse<RechargeOrderVo> res = new BaseResponse<>();
        RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("orgNo" , orgNo);
        paramMap.put("orgOrderNo" , orgOrderNo);
        ServiceOrder serviceOrder = orderDao.selectSingle(paramMap);
        if (null == serviceOrder){
            res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
            res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
            return res;
        }
        paramMap.clear();
        paramMap.put("orderId" , serviceOrder.getId());
        ServiceOrderRecharge rechargeOrder = orderRechargeDao.selectSingle(paramMap);
        if(null == rechargeOrder){
            res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
            res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
            return res;
        }
        rechargeOrderVo.setOrder(serviceOrder);
        rechargeOrderVo.setRechargeOrder(rechargeOrder);
        res.setData(rechargeOrderVo);
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

    public BaseResponse<RechargeOrderVo> getRechargeOrderByOrderNo(String orderNo) {
        BaseResponse<RechargeOrderVo> res = new BaseResponse<>();
        RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
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
        ServiceOrderRecharge rechargeOrder =  orderRechargeDao.selectSingle(paramMap);
        if(null == rechargeOrder) {
            res.setResponseCode(ResponseCodeMsg.RESULT_QUERY_EMPTY.getCode());
            res.setResponseMsg(ResponseCodeMsg.RESULT_QUERY_EMPTY.getMsg());
            return res;
        }
        rechargeOrderVo.setOrder(order);
        rechargeOrderVo.setRechargeOrder(rechargeOrder);
        res.setData(rechargeOrderVo);
        return res;
    }

    public Integer updateServiceOrder(ServiceOrder order){
        Integer i = orderDao.update(order);
        return i;
    }*/
}
