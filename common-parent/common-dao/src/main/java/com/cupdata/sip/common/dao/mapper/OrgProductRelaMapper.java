package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.OrgProductRela;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface OrgProductRelaMapper extends Mapper<OrgProductRela> {


    @Select("SELECT * FROM `org_product_rela` WHERE ORG_NO = #{orgNo} AND PRODUCT_NO = #{productNo};")
    OrgProductRela selectRealByorgNoAndproductNo(@Param("orgNo") String orgNo, @Param("productNo") String productNo);

}