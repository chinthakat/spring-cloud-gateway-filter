package com.chinthakat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.chinthakat.gateway.filter","com.chinthakat.gateway"})
@SpringBootApplication
public class SpringCloudGatewayFilterTest {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGatewayFilterTest.class, args);
	}

}
