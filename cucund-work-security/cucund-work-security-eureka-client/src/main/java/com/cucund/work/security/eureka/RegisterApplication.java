package com.cucund.work.security.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.cucund.work.security.annotation.config.AnnotationConfig;
import com.cucund.work.security.eureka.comfig.EurekaConfig;

@SpringBootApplication
@Import(value={EurekaConfig.class,AnnotationConfig.class})
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}
}
