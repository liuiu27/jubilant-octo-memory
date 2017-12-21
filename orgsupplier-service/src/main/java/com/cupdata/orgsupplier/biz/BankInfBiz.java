package com.cupdata.orgsupplier.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.BankInf;
import com.cupdata.commons.model.OrgInf;
import com.cupdata.orgsupplier.dao.BankInfDao;
import com.cupdata.orgsupplier.dao.OrgInfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
