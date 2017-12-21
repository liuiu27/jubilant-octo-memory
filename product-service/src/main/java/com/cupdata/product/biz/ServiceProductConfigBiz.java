package com.cupdata.product.biz;


import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.commons.model.ServiceProductConfig;
import com.cupdata.product.dao.ServiceProductConfigDao;
import com.cupdata.product.dao.ServiceProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
