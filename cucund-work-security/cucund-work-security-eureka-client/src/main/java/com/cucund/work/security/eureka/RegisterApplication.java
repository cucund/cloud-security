package com.cucund.work.security.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

import com.cucund.work.security.annotation.config.AnnotationConfig;

@SpringBootApplication
@EnableEurekaClient
@Import(value={AnnotationConfig.class})
public class RegisterApplication {
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}
}
 