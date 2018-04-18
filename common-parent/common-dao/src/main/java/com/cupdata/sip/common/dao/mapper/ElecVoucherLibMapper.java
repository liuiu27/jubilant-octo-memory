package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.ElecVoucherLib;
import tk.mybatis.mapper.common.Mapper;

public interface ElecVoucherLibMapper extends Mapper<ElecVoucherLib> {

    /**
     * 根据券码类别id获取有效券码
     * @param categoryId
     * @return
     */
    ElecVoucherLib selectValidVoucherLibByCategoryId(Long categoryId);

    /**
     * 更新券码列表
     * @param elecVoucherLib
     */
    void UpdateElectronicVoucherLib(ElecVoucherLib elecVoucherLib);
}