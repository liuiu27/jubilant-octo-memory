package com.cupdata.trvok.feign;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.config.IConfigController;

@FeignClient(name = "config-service")
public interface ConfigFeignClient extends IConfigController{
}
