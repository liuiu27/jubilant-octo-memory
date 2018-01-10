package com.cupdata.config.dao;

import org.apache.ibatis.annotations.Mapper;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
@Mapper
public interface ConfigDao extends BaseDao<SysConfig>{

}
