package com.cupdata.order.dao;

import com.cupdata.order.entity.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;
import com.cupdata.commons.dao.BaseDao;

@Mapper
public interface ServiceOrderDao extends BaseDao<ServiceOrder>{
	 
}