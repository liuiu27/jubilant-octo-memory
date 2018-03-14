package com.cupdata.order.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.vo.content.ServiceOrderContent;
import com.cupdata.order.dao.ServiceOrderContentDao;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月13日 下午3:37:08
*/
@Service
public class ServiceOrderContentBiz extends BaseBiz<ServiceOrderContent>{
	@Autowired
	private ServiceOrderContentDao serviceOrderContentDao;
	
	@Override
	public BaseDao<ServiceOrderContent> getBaseDao() {
		return serviceOrderContentDao;
	}
	
}
