package com.cucund.security.permission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.cucund.security.mybatis.config.MybatisConfig;
import com.cucund.work.security.amq.config.ActiveMqConfig;
import com.cucund.work.security.eureka.comfig.EurekaConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import(value={ActiveMqConfig.class,MybatisConfig.class,EurekaConfig.class})
public class RegisterApplication {
	
	@Bean
	public ActiveMqConfig activeMqConfig(){
		return new ActiveMqConfig();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(RegisterApplication.class, args);
	}
	
	
}
