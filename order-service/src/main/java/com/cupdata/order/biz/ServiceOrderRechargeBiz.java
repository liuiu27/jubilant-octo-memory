package com.cupdata.order.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cupdata.order.util.OrderUtils;
import com.cupdata.sip.common.api.order.request.CreateOrderVo;
import com.cupdata.sip.common.api.order.response.RechargeOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.entity.ServiceOrderRecharge;
import com.cupdata.sip.common.dao.entity.ServiceOrderVoucher;
import com.cupdata.sip.common.dao.mapper.ServiceOrderMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderRechargeMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: liwei
 * @Description:
 * @Date:2018/04/18
 */
@Slf4j
@Service
public class ServiceOrderRechargeBiz{
	
	@Autowired
	private ServiceOrderMapper orderDao;
	
	@Autowired
	private ServiceOrderRechargeMapper orderRechargeDao;

	public RechargeOrderVo getRechargeOrderByOrderNo(String orderNo) {
		RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
		
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrderNo(orderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		
		if(null == serviceOrder) {
			
		}
		
		ServiceOrderRecharge serviceOrderRecharge = new ServiceOrderRecharge();
		serviceOrderRecharge.setOrderId(serviceOrder.getId());
		serviceOrderRecharge = orderRechargeDao.selectOne(serviceOrderRecharge);
		
		if(null == serviceOrderRecharge) {
			
		}
		
		BeanCopierUtils.copyProperties(serviceOrder,rechargeOrderVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(serviceOrderRecharge,rechargeOrderVo);
		
		return rechargeOrderVo;
	}

	public RechargeOrderVo getRechargeOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
		RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
		
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrgNo(orgNo);
		serviceOrder.setOrgOrderNo(orgOrderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		
		if(null == serviceOrder) {
			
		}
		
		ServiceOrderRecharge serviceOrderRecharge = new ServiceOrderRecharge();
		serviceOrderRecharge.setOrderId(serviceOrder.getId());
		serviceOrderRecharge = orderRechargeDao.selectOne(serviceOrderRecharge);
		
		if(null == serviceOrderRecharge) {
			
		}
		
		BeanCopierUtils.copyProperties(serviceOrder,rechargeOrderVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(serviceOrderRecharge,rechargeOrderVo);
		
		return rechargeOrderVo;
	}
	
	/**
	 * 创建充值订单
	 * @param supplierFlag
	 * @param orgNo
	 * @param orgOrderNo
	 * @param orderDesc
	 * @param data
	 * @param data2
	 * @return
	 */
	@Transactional
	public RechargeOrderVo createRechargeOrder(String accountNumber,String supplierFlag, String orgNo, String orgOrderNo, String orderDesc,
			ProductInfoVo rechargeProduct, OrgProductRelVo orgProductRelVo) {
		   log.info("创建券码订单,supplierFlag:"+supplierFlag+",orgNo:"+orgNo+",orderDesc:"+orderDesc);
		   RechargeOrderVo rechargeOrderVo =new RechargeOrderVo();
	        //初始化主订单记录
		    CreateOrderVo createOrderVo = new CreateOrderVo();
		    createOrderVo.setOrgNo(orgNo);
		    createOrderVo.setOrderDesc(orderDesc);
	        ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag, createOrderVo, rechargeProduct, orgProductRelVo);
	        orderDao.insert(order);//插入主订单

	        //初始化充值订单
	        ServiceOrderRecharge orderRecharge = OrderUtils.initRechargeOrder(accountNumber, rechargeProduct, order);
	        orderRechargeDao.insert(orderRecharge);//插入券码订单

	        BeanCopierUtils.copyProperties(order,rechargeOrderVo.getOrderInfoVo());
	        BeanCopierUtils.copyProperties(orderRecharge,rechargeOrderVo);

	        return rechargeOrderVo;
	}

	public void updateRechargeOrder(RechargeOrderVo rechargeOrderVo) {
		ServiceOrder serviceOrder = new ServiceOrder();
		
		serviceOrder.setId(rechargeOrderVo.getOrderId());
		serviceOrder = orderDao.selectOne(serviceOrder);
		if(null==serviceOrder) {
			//TODO throws
		}
		BeanCopierUtils.copyProperties(rechargeOrderVo.getOrderInfoVo(),serviceOrder);
		orderDao.updateByPrimaryKey(serviceOrder);
		
		ServiceOrderRecharge  servicseOrderRecharge = new ServiceOrderRecharge();
		servicseOrderRecharge.setOrderId(rechargeOrderVo.getOrderId());
		servicseOrderRecharge = orderRechargeDao.selectOne(servicseOrderRecharge);
		
		if(null == servicseOrderRecharge) {
			//TODO throws
		}
		
		BeanCopierUtils.copyProperties(rechargeOrderVo,servicseOrderRecharge);
		
		//TODO
		orderRechargeDao.updateByPrimaryKey(servicseOrderRecharge);
		orderRechargeDao.updateByPrimaryKeySelective(servicseOrderRecharge);
		
	}
	
	
	
}	  

