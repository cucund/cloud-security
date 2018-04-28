package com.cucund.security.premission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.cucund.work.security.amq.config.ActiveMqConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import(ActiveMqConfig.class)
public class RegisterApplication {
	
	@Bean
	public ActiveMqConfig activeMqConfig(){
		return new ActiveMqConfig();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(RegisterApplication.class, args);
	}
	
	
}
