package com.cupdata.ihuyi.feign;


import com.cupdata.commons.api.cache.ICacheController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "cache-service")
public interface CacheFeignClient extends ICacheController{
}
