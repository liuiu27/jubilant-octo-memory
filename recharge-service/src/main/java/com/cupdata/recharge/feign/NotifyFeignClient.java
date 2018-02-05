package com.cupdata.recharge.feign;


import com.cupdata.commons.api.notify.INotifyController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "notify-service")
public interface NotifyFeignClient extends INotifyController{
}
