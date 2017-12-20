package com.cupdata.order.rpc;


import com.cupdata.commons.api.product.IProductController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("product-service")
public interface ProductServiceClient extends IProductController{
}
