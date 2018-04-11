package com.cupdata.sip.common.dao.mapper;

import com.cupdata.sip.common.dao.entity.BankInf;
import tk.mybatis.mapper.common.Mapper;

public interface BankInfMapper extends Mapper<BankInf> {

    BankInf findBankByBankCode(String bankCode);

}