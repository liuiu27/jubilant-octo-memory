package com.cupdata.order.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cupdata.order.util.OrderUtils;
import com.cupdata.sip.common.api.order.request.CreateOrderVo;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.OrderInfoVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.dao.entity.ServiceOrder;
import com.cupdata.sip.common.dao.entity.ServiceOrderVoucher;
import com.cupdata.sip.common.dao.mapper.ServiceOrderMapper;
import com.cupdata.sip.common.dao.mapper.ServiceOrderVoucherMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @Auth: liwei
 * @Description:
 * @Date:2018/04/18
 */

@Slf4j
@Service
public class ServiceOrderVoucherBiz {
    @Autowired
    private ServiceOrderMapper orderDao;

    @Autowired
    private ServiceOrderVoucherMapper orderVoucherDao;
    
    /**
     * 创建券码订单
     * @param supplierFlag
     * @param orgNo
     * @return
     */
    @Transactional
    public VoucherOrderVo createVoucherOrder(String supplierFlag,CreateVoucherOrderVo createVoucherOrderVo, ProductInfoVo voucherProduct, OrgProductRelVo orgProductRela){
        log.info("创建券码订单,supplierFlag:"+supplierFlag+",orgNo:"+createVoucherOrderVo.getOrgNo()+",orderDesc:"+createVoucherOrderVo.getOrderDesc());
        VoucherOrderVo voucherOrderVo =new VoucherOrderVo();
        //初始化主订单记录
        CreateOrderVo createOrderVo = new CreateOrderVo();
        BeanCopierUtils.copyProperties(createVoucherOrderVo,createOrderVo);
        
        ServiceOrder order = OrderUtils.initServiceOrder(supplierFlag ,createOrderVo,voucherProduct, orgProductRela);
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
  	
  	@Transactional
	public void updateVoucherOrder(VoucherOrderVo voucherOrderVo) {
		ServiceOrder serviceOrder = new ServiceOrder();
		
		serviceOrder.setId(voucherOrderVo.getOrderId());
		serviceOrder = orderDao.selectOne(serviceOrder);
		if(null==serviceOrder) {
			//TODO throws
		}
		BeanCopierUtils.copyProperties(voucherOrderVo.getOrderInfoVo(),serviceOrder);
		orderDao.updateByPrimaryKey(serviceOrder);
		
		ServiceOrderVoucher  serviceOrderVoucher = new ServiceOrderVoucher();
		serviceOrderVoucher.setOrderId(voucherOrderVo.getOrderId());
		serviceOrderVoucher.setVoucherCode(voucherOrderVo.getVoucherCode());
		serviceOrderVoucher = orderVoucherDao.selectOne(serviceOrderVoucher);
		
		if(null == serviceOrderVoucher) {
			//TODO throws
		}
		
		BeanCopierUtils.copyProperties(voucherOrderVo,serviceOrderVoucher);
		
		//TODO
		orderVoucherDao.updateByPrimaryKey(serviceOrderVoucher);
		orderVoucherDao.updateByPrimaryKeySelective(serviceOrderVoucher);
	}
  	
	public VoucherOrderVo getVoucherOrderByOrderNo(String orderNo) {
		
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrderNo(orderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		
		if(null == serviceOrder) {
			
		}
		
		ServiceOrderVoucher  serviceOrderVoucher = new ServiceOrderVoucher();
		serviceOrderVoucher.setOrderId(serviceOrder.getId());
		serviceOrderVoucher = orderVoucherDao.selectOne(serviceOrderVoucher);
		
		BeanCopierUtils.copyProperties(serviceOrder,voucherOrderVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(serviceOrderVoucher,voucherOrderVo);
		
		return voucherOrderVo;
	}

	public VoucherOrderVo getVoucherOrderByOrgNoAndOrgOrderNo(String orgNo, String orgOrderNo) {
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setOrgNo(orgNo);
		serviceOrder.setOrgOrderNo(orgOrderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		
		if(null == serviceOrder) {
			
		}
		
		ServiceOrderVoucher  serviceOrderVoucher = new ServiceOrderVoucher();
		serviceOrderVoucher.setOrderId(serviceOrder.getId());
		serviceOrderVoucher = orderVoucherDao.selectOne(serviceOrderVoucher);
		
		BeanCopierUtils.copyProperties(serviceOrder,voucherOrderVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(serviceOrderVoucher,voucherOrderVo);
		
		return voucherOrderVo;
	}

	public VoucherOrderVo getVoucherOrderByVoucher(String sup, String supplierOrderNo, String voucherCode) {
		VoucherOrderVo voucherOrderVo = new VoucherOrderVo();
		
		ServiceOrder serviceOrder = new ServiceOrder();
		serviceOrder.setSupplierNo(sup);
		serviceOrder.setSupplierOrderNo(supplierOrderNo);
		serviceOrder = orderDao.selectOne(serviceOrder);
		
		if(null == serviceOrder) {
			
		}
		
		ServiceOrderVoucher  serviceOrderVoucher = new ServiceOrderVoucher();
		serviceOrderVoucher.setOrderId(serviceOrder.getId());
		serviceOrderVoucher.setVoucherCode(voucherCode);
		serviceOrderVoucher = orderVoucherDao.selectOne(serviceOrderVoucher);
		
		BeanCopierUtils.copyProperties(serviceOrder,voucherOrderVo.getOrderInfoVo());
		BeanCopierUtils.copyProperties(serviceOrderVoucher,voucherOrderVo);
		
		return voucherOrderVo;
	}
    
}

