package com.cupdata.content.feign;


import com.cupdata.sip.common.api.product.IProductController;
import com.cupdata.sip.common.api.product.ProductControllerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "product-service", fallbackFactory = ProductControllerFallback.class)
public interface ProductFeignClient extends IProductController {
}
