package com.cupdata.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.cupdata.notify.listener.ApplicationReadyEventListener;
/**
 * @Auth: liwei
 * @Description:
 * @Date: 15:23 2018/1/11
 */

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class NotifyServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(NotifyServiceApplication.class);
		app.addListeners(new ApplicationReadyEventListener());//监听器
		app.run(args);
	}
}
