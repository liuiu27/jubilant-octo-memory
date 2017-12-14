package com.cupdate.order.biz;


import com.cupdate.order.dao.ServiceOrderDao;
import com.cupdate.order.domain.ServiceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBiz {


    @Autowired
    private ServiceOrderDao serviceOrderDao;


    /**查询订单表*/
    public   List<ServiceOrder> findServiceOrder(ServiceOrder serviceOrder){
       return serviceOrderDao.findServiceOrder(serviceOrder);
    }




}
