package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.ServiceSupplier;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ServiceSupplierMapper extends Mapper<ServiceSupplier> {

    ServiceSupplier findSupByNo(@Param("supplierNo") String supplierNo);
}