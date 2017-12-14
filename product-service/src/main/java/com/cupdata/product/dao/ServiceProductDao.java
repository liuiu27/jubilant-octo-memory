package com.cupdata.product.dao;

import com.cupdata.commapi.dao.BaseDao;
import com.cupdata.product.domain.ServiceProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServiceProductDao extends BaseDao<ServiceProduct>{

}