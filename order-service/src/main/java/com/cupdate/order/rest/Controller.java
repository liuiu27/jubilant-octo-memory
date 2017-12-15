package com.cupdate.order.rest;

import com.cupdata.commapi.ServiceA;
import com.cupdate.order.biz.OrderBiz;
import com.cupdate.order.domain.ServiceOrder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class Controller implements ServiceA {


    @Resource
    private OrderBiz orderBiz;


    @Override
    public String hello() {
        return "hello spring cloud";

    }
}
