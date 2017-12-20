package com.cupdata.apigateway;

import com.cupdata.apigateway.filters.post.ResponseFilter;
import com.cupdata.apigateway.filters.pre.PreRequestFilter;
import com.cupdata.apigateway.filters.pre.RequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public PreRequestFilter preRequestLogFilter() {
		return new PreRequestFilter();
	}

	public ResponseFilter responseFilter(){
		return new ResponseFilter();
	}

	@Bean
	public RequestFilter requestFilter(){
		return new RequestFilter();
	}
}
