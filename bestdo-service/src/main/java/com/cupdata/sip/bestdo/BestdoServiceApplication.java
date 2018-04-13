package com.cupdata.sip.bestdo;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringCloudApplication
@EnableFeignClients
@EnableCaching
public class BestdoServiceApplication {
    public static void main(String args[]) {
        SpringApplication.run(BestdoServiceApplication.class, args);
    }

}