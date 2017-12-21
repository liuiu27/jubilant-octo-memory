package com.cupdata.recharge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RechargeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RechargeServiceApplication.class, args);
	}
}
