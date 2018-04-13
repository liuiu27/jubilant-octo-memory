package com.cupdata.apigateway.feign;

import com.cupdata.sip.common.api.orgsup.IOrgController;
import com.cupdata.sip.common.api.orgsup.OrgControllerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "orgsupplier-service",fallback = OrgControllerFallback.class)
public interface OrgFeignClient extends IOrgController {

}
