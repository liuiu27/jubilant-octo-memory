package com.cupdata.voucher.feign;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.notify.INotifyController;

@FeignClient(name = "notify-service")
public interface NotifyFeignClient extends INotifyController{
}
