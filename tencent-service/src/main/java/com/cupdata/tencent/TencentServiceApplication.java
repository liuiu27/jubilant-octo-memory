package com.cupdata.tencent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Description: 腾讯相关服务启动入口
 * @Author: Dcein
 * @CreateDate: 2018/2/1 9:48
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class TencentServiceApplication {
    public static void main(String args[]){
        SpringApplication.run(TencentServiceApplication.class,args);}
}
