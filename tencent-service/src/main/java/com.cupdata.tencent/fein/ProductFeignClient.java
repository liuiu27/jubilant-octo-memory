package com.cupdata.tencent.fein;


import com.cupdata.commons.api.product.IProductController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductFeignClient extends IProductController{
}
