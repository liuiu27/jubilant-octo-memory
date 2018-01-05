package com.cupdata.cache.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.cache.dao.BankInfDao;
import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.BankInf;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class BankInfBiz extends BaseBiz<BankInf> {
    @Autowired
    private BankInfDao bankInfDao;


    @Override
    public BaseDao<BankInf> getBaseDao() {
        return bankInfDao;
    }

}
