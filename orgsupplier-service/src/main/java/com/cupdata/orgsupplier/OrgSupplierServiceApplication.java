package com.cupdata.orgsupplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrgSupplierServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrgSupplierServiceApplication.class, args);
	}
}
