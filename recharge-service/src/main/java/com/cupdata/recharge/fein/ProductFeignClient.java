package com.cupdata.recharge.fein;


import com.cupdata.commons.api.product.IProductController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = "product-service", fallbackFactory = ProductFeignClientFallbackFactory.class)
public interface ProductFeignClient extends IProductController{
}

@Component
class ProductFeignClientFallbackFactory implements FallbackFactory<ProductFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFeignClientFallbackFactory.class);

    @Override
    public ProductFeignClient create(final Throwable throwable) {
        return new ProductFeignClient(){

            @Override
            public BaseResponse<ProductInfVo> findByProductNo(String productNo) {
                ProductFeignClientFallbackFactory.LOGGER.error("调用产品服务应用product-service的findByProductNo方法出现问题，执行fallback程序", throwable);
                return null;
            }

            @Override
            public BaseResponse<OrgProductRelVo> findRel(String orgNo, String productNo) {
                ProductFeignClientFallbackFactory.LOGGER.error("调用产品服务应用product-service的findRel方法出现问题，执行fallback程序", throwable);

                return null;
            }
        };
    }
}