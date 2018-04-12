package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.ServiceProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface ServiceProductMapper extends Mapper<ServiceProduct> {


    @Select("SELECT * FROM `service_product` where 1=1 AND PRODUCT_NO = #{productNo}")
    ServiceProduct selectByProductNo(@Param("productNo") String productNo);
}