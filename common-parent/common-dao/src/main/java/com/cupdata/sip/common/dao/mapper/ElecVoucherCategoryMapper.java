package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.ElecVoucherCategory;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ElecVoucherCategoryMapper extends Mapper<ElecVoucherCategory> {

    /**
     * 根据券码分类id查询有效券码
     * @param categoryId
     * @return
     */
    ElecVoucherCategory getValidVoucherById(@Param("categoryId") Long categoryId);

}