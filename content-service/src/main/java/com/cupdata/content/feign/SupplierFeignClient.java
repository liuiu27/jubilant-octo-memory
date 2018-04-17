package com.cupdata.content.feign;

import com.cupdata.sip.common.api.orgsup.ISupplierApi;
import com.cupdata.sip.common.api.orgsup.SupplierApiFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author Tony
 * @date 2018/04/10
 */
@FeignClient(name = "orgsupplier-service", fallback = SupplierApiFallback.class)
public interface SupplierFeignClient extends ISupplierApi {


}
