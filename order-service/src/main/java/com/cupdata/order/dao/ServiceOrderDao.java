package com.cupdata.order.dao;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ServiceOrderDao extends BaseDao<ServiceOrder>{


    /**
     * 根据订单状态，支付状态，商户标识，商品类型查询主订单
     * @param paraMap
     * @return
     */
     List<ServiceOrder> selectMainOrderList(HashMap<String, Object> paraMap);
}