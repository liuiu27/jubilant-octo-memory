package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.SysConfig;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysConfigMapper extends Mapper<SysConfig> {
    SysConfig getSysConfig(@Param("paraName") String paraName, @Param("bankCode") String bankCode);
}