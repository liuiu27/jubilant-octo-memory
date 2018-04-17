package com.cupdata.cache;

import com.cupdata.cache.listener.ApplicationReadyEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class EhCacheServiceApplication{

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EhCacheServiceApplication.class);
		app.addListeners(new ApplicationReadyEventListener());//监听器
		app.run(args);
	}
}
