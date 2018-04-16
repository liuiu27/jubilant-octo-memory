package com.cupdata.sip.bestdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableDiscoveryClient
public class BestdoServiceApplication {

    public static void main(String args[]) {
        SpringApplication.run(BestdoServiceApplication.class, args);
    }

}