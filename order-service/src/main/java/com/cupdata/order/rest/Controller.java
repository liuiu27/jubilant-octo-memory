package com.cupdata.order.rest;


import com.cupdata.commons.api.order.OrderService;
import com.cupdata.order.biz.OrderBiz;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class Controller implements OrderService {


    @Resource
    private OrderBiz orderBiz;


    @Override
    public String hello() {
        return "hello spring cloud";

    }

}
