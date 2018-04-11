package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.BankInf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface BankInfMapper extends Mapper<BankInf> {

    @Select("SELECT * FROM `bank_inf` WHERE BANK_CODE = #{bankCode} ")
    BankInf findBankByBankCode(@Param("bankCode") String bankCode);

}