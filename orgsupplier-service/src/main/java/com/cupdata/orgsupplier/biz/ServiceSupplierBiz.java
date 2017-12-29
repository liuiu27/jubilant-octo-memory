package com.cupdata.orgsupplier.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceSupplier;
import com.cupdata.orgsupplier.dao.ServiceSupplierDao;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceSupplierBiz extends BaseBiz<ServiceSupplier> {
    @Autowired
    private ServiceSupplierDao serviceSupplierDao;


    @Override
    public BaseDao<ServiceSupplier> getBaseDao() {
        return serviceSupplierDao;
    }

}
