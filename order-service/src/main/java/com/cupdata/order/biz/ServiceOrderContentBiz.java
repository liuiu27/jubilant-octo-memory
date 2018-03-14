package com.cupdata.order.biz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;
import com.cupdata.commons.vo.content.CreateContentOrderVo;
import com.cupdata.commons.vo.content.ServiceOrderContent;
import com.cupdata.order.dao.ServiceOrderContentDao;
import com.cupdata.order.dao.ServiceOrderDao;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午3:37:08
*/
@Service
public class ServiceOrderContentBiz extends BaseBiz<ServiceOrderContent>{
	@Autowired
	private ServiceOrderContentDao serviceOrderContentDao;
	
	@Autowired
	private ServiceOrderDao serviceOrderDao;
	
	@Override
	public BaseDao<ServiceOrderContent> getBaseDao() {
		return serviceOrderContentDao;
	}
	
	/**
	 * 查询内容引入子订单
	 * @param contentQueryOrderReq
	 */
	public ContentQueryOrderRes queryContentOrder(ContentQueryOrderReq contentQueryOrderReq) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		ServiceOrderContent serviceOrderContent =	serviceOrderContentDao.selectSingle(paramMap);
		if(null == serviceOrderContent) {
			return null;
		}
		ServiceOrder serviceOrder = serviceOrderDao.select(serviceOrderContent.getOrderId());
		if(null == serviceOrder) {
			return null;
		}
		ContentQueryOrderRes contentQueryOrderRes = new ContentQueryOrderRes();
		contentQueryOrderRes.setResultCode(String.valueOf(serviceOrder.getOrderStatus()));
		contentQueryOrderRes.setSipOrderNo(serviceOrder.getOrderNo());
		contentQueryOrderRes.setSupOrderNo(serviceOrder.getSupplierOrderNo());
		return contentQueryOrderRes;
	}
	
}
