package com.cupdata.product.biz;


import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.product.dao.ServiceProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

//@Service
public class ServiceProductBiz extends BaseBiz<ServiceProduct> {
    @Autowired
    private ServiceProductDao productDao;

    @Override
    public BaseDao<ServiceProduct> getBaseDao() {
        return productDao;
    }

    /**
     * 根据商品编号，查询商品信息
     * @param productNo
     * @return
     */
    public ServiceProduct selectByProductNo(String productNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productNo", productNo);
        return productDao.selectSingle(params);
    }
}
