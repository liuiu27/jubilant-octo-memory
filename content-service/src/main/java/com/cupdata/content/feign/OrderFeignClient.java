package com.cupdata.content.feign;


import com.cupdata.sip.common.api.order.IOrderController;
import com.cupdata.sip.common.api.order.OrderControllerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "order-service",fallback = OrderControllerFallback.class)
public interface OrderFeignClient extends IOrderController {

}
