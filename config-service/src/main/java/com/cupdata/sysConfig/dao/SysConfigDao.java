package com.cupdata.sysConfig.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.SysConfig;
@Mapper
public interface SysConfigDao extends BaseDao<SysConfig>{

	public String getSysConfig(@Param("bankCode")String bankCode, @Param("paraName")String paraName);
	
}
