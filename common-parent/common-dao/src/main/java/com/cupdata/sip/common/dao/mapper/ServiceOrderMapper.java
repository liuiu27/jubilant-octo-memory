package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.ServiceOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.HashMap;
import java.util.List;

public interface ServiceOrderMapper extends Mapper<ServiceOrder> {

    List<ServiceOrder> selectMainOrderList(@Param("paramMap") HashMap<String, Object> paramMap);

    Long insertOrder(ServiceOrder serviceOrder);
}