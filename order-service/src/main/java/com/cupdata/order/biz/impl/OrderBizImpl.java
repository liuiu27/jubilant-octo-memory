package com.cupdata.order.biz.impl;

import com.cupdata.commons.biz.impl.BaseBizImpl;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.order.biz.OrderBiz;
import com.cupdata.order.dao.ServiceOrderDao;
import com.cupdata.order.entity.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class OrderBizImpl extends BaseBizImpl<ServiceOrder> implements OrderBiz {
    @Autowired
    private ServiceOrderDao orderDao;

    @Override
    public BaseDao<ServiceOrder> getBaseDao() {
        return orderDao;
    }
}
