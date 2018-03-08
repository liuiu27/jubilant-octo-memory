package com.cupdata.ihuyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Author: DingCong
 * @Description: 互亿服务启动接口
 * @CreateDate: 2018/3/6 14:48
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class IhuyiServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(IhuyiServiceApplication.class, args);
    }

}
