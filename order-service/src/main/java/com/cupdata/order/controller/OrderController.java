package com.cupdata.order.controller;

import com.cupdata.commons.api.order.IOrderController;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.order.biz.ServiceOrderBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderController implements IOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private ServiceOrderBiz orderBiz;

    @Override
    public ServiceOrder findOrderById(@PathVariable("id") Long id) {
        return null;
    }
}
