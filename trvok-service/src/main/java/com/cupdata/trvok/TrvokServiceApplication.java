package com.cupdata.trvok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class TrvokServiceApplication {
	
	/**
	 * @LoadBalanced，整合ribbon，使其具备负载均衡的能力
	 * @return
	 */
//	@Bean
//	@LoadBalanced
//	public RestTemplate restTemplate() {
//		return new RestTemplate();
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(TrvokServiceApplication.class, args);
	}
}
