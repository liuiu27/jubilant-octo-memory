package com.cupdata.iqiyi.feign;


import com.cupdata.sip.common.api.config.IConfigController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "config-service")
public interface ConfigFeignClient extends IConfigController{
}
