package com.cupdata.product.biz;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceProductConfigBiz extends BaseBiz<ServiceProductConfig> {
    @Autowired
    private ServiceProductConfigDao productConfigDao;

    @Override
    public BaseDao<ServiceProductConfig> getBaseDao() {
        return productConfigDao;
    }
}
