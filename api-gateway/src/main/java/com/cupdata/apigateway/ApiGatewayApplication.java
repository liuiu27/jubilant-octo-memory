package com.cupdata.apigateway;

<<<<<<< HEAD
import com.cupdata.apigateway.filters.pre.PreRequestFilter;
=======
import com.cupdata.apigateway.filter.RequestFilter;
import com.cupdata.apigateway.filter.ResponseFilter;
>>>>>>> 52011d1b8cf7d8174132b2a9419f8fbc3cc1f9b7
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
<<<<<<< HEAD
import org.springframework.web.client.RestTemplate;
=======
>>>>>>> 52011d1b8cf7d8174132b2a9419f8fbc3cc1f9b7


@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
<<<<<<< HEAD
	public PreRequestFilter preRequestLogFilter() {
		return new PreRequestFilter();
	}

	/**
	 * @LoadBalanced，整合ribbon，使其具备负载均衡的能力
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
=======
	public ResponseFilter responseFilter(){
		return new ResponseFilter();
	}

	@Bean
	public RequestFilter requestFilter(){
		return new RequestFilter();
	}

>>>>>>> 52011d1b8cf7d8174132b2a9419f8fbc3cc1f9b7
}
