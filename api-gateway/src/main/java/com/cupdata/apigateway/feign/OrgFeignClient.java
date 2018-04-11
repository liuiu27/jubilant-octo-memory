package com.cupdata.apigateway.feign;

import com.cupdata.sip.common.api.orgsup.IOrgController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "orgsupplier-service")
public interface OrgFeignClient extends IOrgController {

}
