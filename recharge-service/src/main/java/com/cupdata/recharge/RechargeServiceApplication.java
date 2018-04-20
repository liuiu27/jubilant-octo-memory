package com.cupdata.recharge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class RechargeServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(RechargeServiceApplication.class, args);
	}

	/**
	 * @LoadBalanced，整合ribbon，使其具备负载均衡的能力
	 * @return
	 */
	@Bean("serverTemplate")
	@LoadBalanced
	public RestTemplate serverTemplate() {
		return new RestTemplate();
	}
}
