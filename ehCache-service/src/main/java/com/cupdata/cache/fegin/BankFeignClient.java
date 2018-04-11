package com.cupdata.cache.fegin;


import org.springframework.cloud.netflix.feign.FeignClient;

import com.cupdata.commons.api.orgsupplier.IBankController;

@FeignClient(name = "orgsupplier-service")
public interface BankFeignClient extends IBankController{
}
