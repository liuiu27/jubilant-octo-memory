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
  
}
