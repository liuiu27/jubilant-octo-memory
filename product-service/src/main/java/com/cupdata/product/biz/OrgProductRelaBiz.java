package com.cupdata.product.biz;


import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.product.dao.OrgProductRelaDao;
import com.cupdata.product.dao.ServiceProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class OrgProductRelaBiz extends BaseBiz<OrgProductRela> {
    @Autowired
    private OrgProductRelaDao orgProductRelaDao;

    @Override
    public BaseDao<OrgProductRela> getBaseDao() {
        return orgProductRelaDao;
    }
}
