package com.cupdate.order.dao;

import com.cupdate.order.domain.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ServiceOrderDao {
	 
	 /**插入订单表数据*/
	 int insertServiceOrder(ServiceOrder serviceOrder);
     
	 /**查询订单表*/
	 List<ServiceOrder> findServiceOrder(ServiceOrder serviceOrder);
	 
	 /**修改订单表*/
	 int updateServiceOrder(ServiceOrder serviceOrder);


	

}