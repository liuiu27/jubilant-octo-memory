package com.cupdata.tencent.feign;


import com.cupdata.commons.api.order.IOrderController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "order-service")
public interface OrderFeignClient extends IOrderController{
}
