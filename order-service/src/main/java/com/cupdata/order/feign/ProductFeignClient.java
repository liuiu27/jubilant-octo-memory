package com.cupdata.order.feign;


import com.cupdata.commons.api.product.IProductController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("product-service")
public interface ProductFeignClient extends IProductController{
}
