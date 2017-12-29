package com.cupdata.orgsupplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgInf;

@Mapper
public interface OrgInfDao extends BaseDao<OrgInf>{
	
	OrgInf findOrgByNo(@Param("orgNo")String orgNo);
	
}