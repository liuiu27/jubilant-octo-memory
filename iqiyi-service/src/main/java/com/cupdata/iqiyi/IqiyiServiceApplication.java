package com.cupdata.iqiyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @Author: DingCong
 * @Description: 爱奇艺启动入口
 * @CreateDate: 2018/2/6 14:49
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class IqiyiServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(IqiyiServiceApplication.class,args);
    }
}
