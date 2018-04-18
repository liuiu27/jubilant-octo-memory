package com.cupdata.recharge.feign;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.IOrgController;
import com.cupdata.sip.common.api.orgsup.OrgControllerFallback;
import com.cupdata.sip.common.api.orgsup.response.OrgInfoVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@FeignClient(name = "orgsupplier-service", fallbackFactory = OrgControllerFallback.class)
public interface OrgFeignClient extends IOrgController {

}

@Component
class OrgFeignClientFallbackFactory implements FallbackFactory<OrgFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgFeignClientFallbackFactory.class);

    @Override
    public OrgFeignClient create(final Throwable throwable) {
        return new OrgFeignClient(){

            @Override
            public BaseResponse<OrgInfoVo> findOrgByNo(String orgNo) {
                return null;
            }

            @Override
            public BaseResponse<List<OrgInfoVo>> selectAll() {
                return null;
            }
        };
    }
}