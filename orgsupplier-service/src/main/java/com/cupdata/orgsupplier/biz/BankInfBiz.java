package com.cupdata.orgsupplier.biz;

import com.cupdata.sip.common.api.orgsup.response.BankInfoVo;
import com.cupdata.sip.common.dao.entity.BankInf;
import com.cupdata.sip.common.dao.mapper.BankInfMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

///**
// * @Auth: LinYong
// * @Description:
// * @Date: 20:20 2017/12/14
// */
//
@Service
public class BankInfBiz {

    @Autowired
    private BankInfMapper bankInfDao;


    public BankInfoVo findBankByBankCode(String bankCode) {

        BankInf bankInf = bankInfDao.findBankByBankCode(bankCode);
        BankInfoVo bankInfoVo =new BankInfoVo();
        BeanCopierUtils.copyProperties(bankInf,bankInfoVo);

        return bankInfoVo;
    }

    public List<BankInfoVo> selectAll() {

        List<BankInf> bankInfs  = bankInfDao.selectAll();

        List<BankInfoVo> bankInfoVos = new ArrayList<>(bankInfs.size());

        bankInfs.stream().forEach(bankInf -> {
            BankInfoVo bankInfoVo = new BankInfoVo();

            BeanCopierUtils.copyProperties(bankInf,bankInfoVo);

            bankInfoVos.add(bankInfoVo);

        });

        return bankInfoVos;
    }
}
