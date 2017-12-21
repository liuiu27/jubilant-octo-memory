package com.cupdata.order.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrderRecharge;
import com.cupdata.commons.model.ServiceOrderVoucher;
import com.cupdata.order.dao.ServiceOrderRechargeDao;
import com.cupdata.order.dao.ServiceOrderVoucherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceOrderVoucherBiz extends BaseBiz<ServiceOrderVoucher> {
    @Autowired
    private ServiceOrderVoucherDao orderVoucherDao;

    @Override
    public BaseDao<ServiceOrderVoucher> getBaseDao() {
        return orderVoucherDao;
    }
}
