package com.cucund.work.security.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import com.cucund.work.security.eureka.comfig.EurekaConfig;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@Import(value={EurekaConfig.class})
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}
	
}
 