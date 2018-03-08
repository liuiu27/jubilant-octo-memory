package com.cupdata.ihuyi.feign;


import com.cupdata.commons.api.cache.ICacheController;
import com.cupdata.commons.api.notify.INotifyController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "notify-service")
public interface NotifyFeignClient extends INotifyController {
}
