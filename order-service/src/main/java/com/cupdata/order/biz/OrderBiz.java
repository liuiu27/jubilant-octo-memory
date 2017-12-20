package com.cupdata.order.biz;

import com.cupdata.commapi.biz.BaseBiz;
import com.cupdata.commapi.dao.BaseDao;
import com.cupdata.commapi.model.ServiceOrder;
import com.cupdata.order.dao.ServiceOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: LinYong
 * @Description:
 * @Date: 20:20 2017/12/14
 */

@Service
public class OrderBiz extends BaseBiz<ServiceOrder> {
    @Autowired
    private ServiceOrderDao orderDao;


    @Override
    public BaseDao<ServiceOrder> getBaseDao() {
        return orderDao;
    }
}
