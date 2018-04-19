package com.cupdata.recharge.feign;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.IProductController;
import com.cupdata.sip.common.api.product.ProductControllerFallback;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = "product-service", fallbackFactory = ProductControllerFallback.class)
public interface ProductFeignClient extends IProductController {
}

@Component
class ProductFeignClientFallbackFactory implements FallbackFactory<ProductFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFeignClientFallbackFactory.class);

    @Override
    public ProductFeignClient create(final Throwable throwable) {
        return new ProductFeignClient(){

            @Override
            public BaseResponse<ProductInfoVo> findByProductNo(String productNo) {
                return null;
            }

            @Override
            public BaseResponse<OrgProductRelVo> findRel(String orgNo, String productNo) {
                return null;
            }
        };
    }
}