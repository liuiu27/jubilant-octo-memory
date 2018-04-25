
package com.cupdata.order.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cupdata.order.util.OrderUtils;
import com.cupdata.sip.common.api.order.request.CreateContentOrderVo;
import com.cupdata.sip.common.api.order.request.CreateOrderVo;
import com.cupdata.sip.common.api.order.response.OrderContentVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.entity.ServiceOrderContent;
import com.cupdata.sip.common.dao.mapper.ServiceOrderContentMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import com.cupdata.sip.common.lang.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午3:37:08
**/

@Slf4j
@Service
public class ServiceOrderContentBiz{
	
	@Autowired
	private ServiceOrderMapper orderDao;
	
	@Autowired
	private ServiceOrderContentMapper orderContentDao;
	
	public OrderContentVo queryContentOrder(String orderNo) {
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrderNo(orderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		if(null == serviceOrder) {
			log.error("queryContentOrder result serviceOrder is null");
			throw new RuntimeException();
		}
		ServiceOrderContent orderContent = new ServiceOrderContent();
		orderContent.setOrderId(serviceOrder.getId());
		orderContent = orderContentDao.selectOne(orderContent);
		
		if(null == orderContent) {
			log.error("queryContentOrder result orderContent is null");
			throw new RuntimeException();
		}
		OrderContentVo orderContentVo = new OrderContentVo();
		BeanCopierUtils.copyProperties(serviceOrder,orderContentVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(orderContent,orderContentVo);
		
		return orderContentVo;
	}
	
	@Transactional
	public OrderContentVo createContentOrder(String supplierFlag, CreateContentOrderVo createContentOrderVo, ProductInfoVo productInfoVo, OrgProductRelVo orgProductRelVo) {
		 	log.info("创建内容引入订单:" +  createContentOrderVo.toString());
		 	
	        //初始化主订单记录
		 	CreateOrderVo createOrderVo = new CreateOrderVo();
		 	BeanCopierUtils.copyProperties(createContentOrderVo,createOrderVo);
		 	
	        ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag ,createOrderVo, productInfoVo, orgProductRelVo);
	        EntityUtils.setEntityInfo(order, EntityUtils.cfields);
	        orderDao.insert(order);//插入主订单

	        //初始内容引入订单
	        ServiceOrderContent  orderContent = OrderUtils.initContentOrder(order, createContentOrderVo);
	        EntityUtils.setEntityInfo(orderContent, EntityUtils.cfields);
	        orderContentDao.insert(orderContent);//插入内容引入订单
	        
	        OrderContentVo  orderContentVo = new OrderContentVo();
	        BeanCopierUtils.copyProperties(order,orderContentVo.getOrderInfoVo());
	        BeanCopierUtils.copyProperties(orderContent,orderContentVo);
	        return orderContentVo;
	}
	
	@Transactional
	public void updateContentOrder(OrderContentVo orderContentVo){
		ServiceOrder serviceOrder = new ServiceOrder();
		
		serviceOrder.setId(orderContentVo.getOrderId());
		serviceOrder = orderDao.selectOne(serviceOrder);
		if(null==serviceOrder) {
			log.error("updateContentOrder serviceOrder is null");
			throw new RuntimeException();
		}
		BeanCopierUtils.copyProperties(orderContentVo.getOrderInfoVo(),serviceOrder);
		EntityUtils.setEntityInfo(serviceOrder, EntityUtils.ufields);
		orderDao.updateByPrimaryKey(serviceOrder);
		
		ServiceOrderContent  serviceOrderContent = new ServiceOrderContent();
		serviceOrderContent.setOrderId(orderContentVo.getOrderId());
		serviceOrderContent = orderContentDao.selectOne(serviceOrderContent);
		
		if(null == serviceOrderContent) {
			log.error("serviceOrderContent serviceOrder is null");
			throw new RuntimeException();
		}
		BeanCopierUtils.copyProperties(orderContentVo,serviceOrderContent);
		EntityUtils.setEntityInfo(serviceOrderContent, EntityUtils.ufields);
		orderContentDao.updateByPrimaryKeySelective(serviceOrderContent);
		
	}
	
}

