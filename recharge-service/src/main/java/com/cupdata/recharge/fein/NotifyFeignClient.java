package com.cupdata.recharge.fein;


import com.cupdata.commons.api.notify.INotifyController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "notify-service")
public interface NotifyFeignClient extends INotifyController{
}
