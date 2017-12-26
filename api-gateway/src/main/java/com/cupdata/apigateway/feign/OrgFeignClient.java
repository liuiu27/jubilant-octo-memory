package com.cupdata.apigateway.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import com.cupdata.commons.api.orgsupplier.IOrgController;

@FeignClient(name = "orgsupplier-service")
public interface OrgFeignClient extends IOrgController {

}
