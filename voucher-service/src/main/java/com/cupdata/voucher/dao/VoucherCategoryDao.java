package com.cupdata.voucher.dao;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ElectronicVoucherCategory;
import com.cupdata.commons.model.ElectronicVoucherLib;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoucherCategoryDao extends BaseDao<ElectronicVoucherCategory> {

}