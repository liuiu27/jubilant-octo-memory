package com.cupdata.orgsupplier.dao;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrgInfDao extends BaseDao<OrgInf>{
	
	OrgInf findOrgByNo(@Param("orgNo")String orgNo);
	
}