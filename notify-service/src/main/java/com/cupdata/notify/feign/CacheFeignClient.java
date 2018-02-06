package com.cupdata.notify.feign;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.cache.ICacheController;

@FeignClient(name = "cache-service")
public interface CacheFeignClient extends ICacheController{
}
