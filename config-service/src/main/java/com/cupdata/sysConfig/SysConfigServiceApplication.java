package com.cupdata.sysConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SysConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysConfigServiceApplication.class, args);
	}
}
