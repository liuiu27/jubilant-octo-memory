package com.cupdata.order.dao;

import org.apache.ibatis.annotations.Mapper;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrder;

@Mapper
public interface ServiceOrderDao extends BaseDao<ServiceOrder>{
	 
}