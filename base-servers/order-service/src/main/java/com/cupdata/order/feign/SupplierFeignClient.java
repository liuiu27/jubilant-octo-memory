package com.cupdata.order.feign;

import com.cupdata.sip.common.api.orgsup.ISupplierApi;
import com.cupdata.sip.common.api.orgsup.SupplierApiFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "orgsupplier-service")
public interface SupplierFeignClient extends ISupplierApi {

}
