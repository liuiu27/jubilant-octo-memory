package com.cupdata.cache.fegin;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.orgsupplier.IBankController;
import com.cupdata.commons.api.orgsupplier.ISupplierController;

@FeignClient(name = "orgsupplier-service")
public interface SupplierFeignClient extends ISupplierController{
}
