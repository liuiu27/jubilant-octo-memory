package com.cupdata.order.feign;

import com.cupdata.commons.api.orgsupplier.IOrgController;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfListVo;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import feign.hystrix.FallbackFactory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 *
 */
@FeignClient(name = "orgsupplier-service", fallbackFactory = OrgFeignClientFallbackFactory.class)
public interface OrgSupplierClient extends IOrgController {

}

@Component
class OrgFeignClientFallbackFactory implements FallbackFactory<OrgSupplierClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgFeignClientFallbackFactory.class);

    @Override
    public OrgSupplierClient create(final Throwable throwable) {
        return new OrgSupplierClient(){

            @Override
            public BaseResponse<OrgInfVo> findOrgByNo(String orgNo) {
                OrgFeignClientFallbackFactory.LOGGER.error("调用机构服务出现问题", throwable);

                return null;
            }

			@Override
			public BaseResponse<OrgInfListVo> selectAll() {
				OrgFeignClientFallbackFactory.LOGGER.error("调用查询机构服务出现问题", throwable);
				return null;
			}
        };
    }
}