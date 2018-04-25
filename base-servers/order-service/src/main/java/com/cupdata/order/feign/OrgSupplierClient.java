package com.cupdata.order.feign;

import com.cupdata.sip.common.api.orgsup.IOrgController;
import com.cupdata.sip.common.api.orgsup.OrgControllerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 *
 */
@FeignClient(name = "orgsupplier-service")
public interface OrgSupplierClient extends IOrgController {


}