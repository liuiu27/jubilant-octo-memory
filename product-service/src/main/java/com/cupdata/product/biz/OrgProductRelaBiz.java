package com.cupdata.product.biz;


import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.OrgProductRela;
import com.cupdata.product.dao.OrgProductRelaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 查询机构与商品关系记录
     * @param orgNo 机构编号
     * @param productNo 商品编号
     * @return
     */
    public OrgProductRela selectReal(String orgNo, String productNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orgNo", orgNo);
        params.put("productNo", productNo);
        return orgProductRelaDao.selectSingle(params);
    }

}
