package com.ms.assignment.ConvertCurrencyMs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author w2cluster03
 *
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
@EnableEurekaClient
@EnableHystrixDashboard
@EnableCircuitBreaker
public class ConvertCurrencyMsApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConvertCurrencyMsApplication.class, args);
	}

}
