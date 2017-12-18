package com.cupdata.order.rpc;


import com.cupdata.commons.api.product.ProductService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("product-service")
public interface ProductServiceClient extends ProductService{
}
