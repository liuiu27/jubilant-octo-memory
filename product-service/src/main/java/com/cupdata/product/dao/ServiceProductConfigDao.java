package com.cupdata.product.dao;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.model.ServiceProductConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceProductConfigDao extends BaseDao<ServiceProductConfig>{

}