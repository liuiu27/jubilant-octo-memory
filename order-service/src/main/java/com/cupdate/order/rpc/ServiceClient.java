package com.cupdate.order.rpc;


import com.cupdata.commapi.ServiceB;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("SERVICEB")
public interface ServiceClient extends ServiceB{
}
