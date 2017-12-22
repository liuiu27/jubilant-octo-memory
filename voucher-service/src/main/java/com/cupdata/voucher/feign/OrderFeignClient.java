package com.cupdata.voucher.feign;


import com.cupdata.commons.api.order.IOrderController;
import com.cupdata.commons.api.product.IProductController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("order-service")
public interface OrderFeignClient extends IOrderController{
}
