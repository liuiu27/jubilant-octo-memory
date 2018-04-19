package com.cupdata.content.feign;

import com.cupdata.sip.common.api.orgsup.IOrgController;
import com.cupdata.sip.common.api.orgsup.OrgControllerFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author Tony
 * @date 2018/04/10
 */
@FeignClient(name = "orgsupplier-service", fallback = OrgControllerFallback.class)
public interface OrgFeignClient extends IOrgController {

}
