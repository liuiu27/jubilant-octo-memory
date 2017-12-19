package com.cupdata.product.biz;

import com.cupdata.commapi.biz.BaseBiz;
import com.cupdata.commapi.dao.BaseDao;
import com.cupdata.commapi.model.ServiceProduct;
import com.cupdata.product.dao.ServiceProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ProductBiz extends BaseBiz<ServiceProduct> {
    @Autowired
    private ServiceProductDao productDao;

    @Override
    public BaseDao<ServiceProduct> getBaseDao() {
        return productDao;
    }
}
