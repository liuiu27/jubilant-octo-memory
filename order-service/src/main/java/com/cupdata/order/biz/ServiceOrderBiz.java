package com.cupdata.order.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.mapper.ServiceOrderMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderRechargeMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderVoucherMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth: liwei
 * @Description: 订单业务
 * @Date: 2018/04/19
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
  	
  	@Transactional
	public void updateServiceOrder(OrderInfoVo order) {
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setOrderNo(order.getOrderNo());
			serviceOrder = orderDao.selectOne(serviceOrder);
			if(null == serviceOrder) {
				
			}
			BeanCopierUtils.copyProperties(order,serviceOrder);
			orderDao.updateByPrimaryKey(serviceOrder);
	}
  	
	public List<OrderInfoVo> selectMainOrderList(String orderStatus, String supplierFlag, String orderSubType) {

		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrderStatus(orderStatus);
		serviceOrder.setSupplierFlag(supplierFlag);
		serviceOrder.setOrderSubType(orderSubType);
		
		List<ServiceOrder> serviceOrderList = orderDao.select(serviceOrder);
		
		if(serviceOrderList ==null || serviceOrderList.isEmpty()) {
			
		}
		List<OrderInfoVo> orderInfoVoList = new ArrayList<OrderInfoVo>(serviceOrderList.size());
		serviceOrderList.forEach(order -> {
			OrderInfoVo infoVo = new OrderInfoVo();
			
			BeanCopierUtils.copyProperties(order,infoVo);
			orderInfoVoList.add(infoVo);		
		} );
		
		return orderInfoVoList;
	}
  
}
