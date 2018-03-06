package com.cupdata.voucher.dao;

import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ElectronicVoucherLib;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoucherLibDao extends BaseDao<ElectronicVoucherLib> {

    /**
     * 根据券码类型id获取有效券码
     * @param voucherLib
     * @return
     */
     ElectronicVoucherLib selectValidVoucherLibByCategoryId(ElectronicVoucherLib voucherLib);

}