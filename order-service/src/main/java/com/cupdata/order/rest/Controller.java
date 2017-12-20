package com.cupdata.order.rest;


import com.cupdata.order.biz.OrderBiz;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class Controller  {


    @Resource
    private OrderBiz orderBiz;


    public String hello() {
        return "hello spring cloud";

    }

}
