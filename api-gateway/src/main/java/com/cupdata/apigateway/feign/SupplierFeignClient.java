package com.cupdata.apigateway.feign;

import com.cupdata.commons.api.orgsupplier.ISupplierController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "orgsupplier-service")
public interface SupplierFeignClient extends ISupplierController {

}
