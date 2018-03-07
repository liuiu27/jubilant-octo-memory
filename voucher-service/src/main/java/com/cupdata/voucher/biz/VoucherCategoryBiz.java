package com.cupdata.voucher.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ElectronicVoucherCategory;
import com.cupdata.commons.model.ElectronicVoucherLib;
import com.cupdata.voucher.dao.VoucherCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */
@Service
public class VoucherCategoryBiz extends BaseBiz<ElectronicVoucherCategory> {

    @Autowired
    private VoucherCategoryDao voucherCategoryDao;

    @Override
    public BaseDao<ElectronicVoucherCategory> getBaseDao() {
        return voucherCategoryDao;
    }

    /**
     * 根据券码类别查询是该类券码是否有效
     * @param categoryId
     * @return
     */
    public  ElectronicVoucherCategory checkVoucherValidStatusByCategoryId(Long categoryId){
        Map<String,Object> params = new HashMap<>();
        params.put("categoryId",categoryId);
        return voucherCategoryDao.selectSingle(params);
    }
}
