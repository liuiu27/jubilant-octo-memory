package com.cupdata.cdd.feign;


import com.cupdata.sip.common.api.order.IOrderController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "order-service")
public interface OrderFeignClient extends IOrderController {
}
