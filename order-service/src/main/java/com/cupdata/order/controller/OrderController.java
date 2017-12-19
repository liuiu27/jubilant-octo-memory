package com.cupdata.order.controller;

import com.cupdata.commons.api.order.IOrderController;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.model.ServiceProduct;
import com.cupdata.order.biz.OrderBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class OrderController implements IOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderBiz orderBiz;

/*    @Autowired
    private ProductServiceClient productServiceClient;*/

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/product/{id}")
    public ServiceProduct findProductById(@PathVariable Long id) {
        return this.restTemplate.getForObject("http://product-service/product/" + id, ServiceProduct.class);
    }

    @GetMapping("/log-product-instance/{id}")
    public ServiceProduct logProductInstance(@PathVariable Long id) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("product-service");
        // 打印当前选择的是哪个节点
        OrderController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
        return this.restTemplate.getForObject("http://product-service/product/" + id, ServiceProduct.class);
    }

    @Override
    public ServiceOrder findOrderById(@PathVariable("id") Long id) {
        return null;
    }
}
