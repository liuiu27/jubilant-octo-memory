package com.cupdata.ihuyi.feign;

import com.cupdata.sip.common.api.orgsup.ISupplierApi;
import com.cupdata.sip.common.api.orgsup.SupplierApiFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "orgsupplier-service", fallback = SupplierApiFallback.class)
public interface SupplierFeignClient extends ISupplierApi {


}
