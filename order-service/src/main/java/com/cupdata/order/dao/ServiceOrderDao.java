package com.cupdata.order.dao;

import com.cupdata.order.domain.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;
import com.cupdata.commons.dao.BaseDao;

@Mapper
public interface ServiceOrderDao extends BaseDao<ServiceOrder>{
	 
}