package com.cupdata.cache.fegin;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.config.IConfigController;

@FeignClient(name = "config-service")
public interface ConfigFeignClient extends IConfigController{
}
