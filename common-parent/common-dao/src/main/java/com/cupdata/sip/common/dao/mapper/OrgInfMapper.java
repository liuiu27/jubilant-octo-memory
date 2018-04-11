package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.OrgInf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface OrgInfMapper extends Mapper<OrgInf> {

    @Select("SELECT * FROM `org_inf` WHERE ORG_NO = #{orgNo}")
    OrgInf findOrgByNo(@Param("orgNo") String orgNo);
}