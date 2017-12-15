package com.cupdata.order.rpc;


import com.cupdata.commapi.apiinterface.ProductService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("product-service")
public interface ProductServiceClient extends ProductService{
}
