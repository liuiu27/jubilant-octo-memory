package com.cupdata.voucher.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ElectronicVoucherLib;
import com.cupdata.voucher.dao.VoucherLibDao;
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
public class VoucherLibBiz extends BaseBiz<ElectronicVoucherLib> {

    @Autowired
    private VoucherLibDao voucherLibDao;

    @Override
    public BaseDao<ElectronicVoucherLib> getBaseDao() {
        return voucherLibDao;
    }

    /**
     * 根据券码类别ID，查询券码信息
     * @param categoryId
     * @return
     */
    public  ElectronicVoucherLib selectVoucherByCategoryId(Long categoryId){

        ElectronicVoucherLib voucherLib = new ElectronicVoucherLib();
        voucherLib.setCategoryId(categoryId);
        return voucherLibDao.selectValidVoucherLibByCategoryId(voucherLib);
    }

    /**
     * 获取券码后更新券码列表中该券码
     */
    public void UpdateElectronicVoucherLib(ElectronicVoucherLib electronicVoucherItem){
        voucherLibDao.update(electronicVoucherItem);
    }
}
