package com.cupdata.order.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.ServiceOrderRecharge;
import com.cupdata.order.dao.ServiceOrderRechargeDao;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class ServiceOrderRechargeBiz extends BaseBiz<ServiceOrderRecharge> {
    @Autowired
    private ServiceOrderRechargeDao orderRechargeDao;

    @Override
    public BaseDao<ServiceOrderRecharge> getBaseDao() {
        return orderRechargeDao;
    }
}
