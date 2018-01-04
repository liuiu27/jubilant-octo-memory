package com.cupdata.cache.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
@Mapper
public interface ConfigDao extends BaseDao<SysConfig>{

	public String getConfig(@Param("bankCode")String bankCode, @Param("paraName")String paraName);

	public List<SysConfig> queryAllConfig();
	
}
