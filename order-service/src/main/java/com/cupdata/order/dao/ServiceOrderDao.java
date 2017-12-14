package com.cupdata.order.dao;

import com.cupdata.order.domain.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;
import com.cupdata.commapi.dao.BaseDao;

import java.util.List;

@Mapper
public interface ServiceOrderDao extends BaseDao<ServiceOrder>{
	 
}