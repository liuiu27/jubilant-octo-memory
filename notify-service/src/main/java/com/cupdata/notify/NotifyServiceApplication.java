package com.cupdata.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * @Auth: liwei
 * @Description:
 * @Date: 15:23 2018/1/11
 */

@SpringBootApplication
@EnableEurekaClient
public class NotifyServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NotifyServiceApplication.class, args);
	}
}
