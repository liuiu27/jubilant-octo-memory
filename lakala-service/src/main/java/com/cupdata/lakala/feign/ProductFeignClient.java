package com.cupdata.lakala.feign;


import com.cupdata.sip.common.api.product.IProductController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductFeignClient extends IProductController {
}
