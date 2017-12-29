package com.cupdata.orgsupplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OrgSupplierServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrgSupplierServiceApplication.class, args);
	}
}
