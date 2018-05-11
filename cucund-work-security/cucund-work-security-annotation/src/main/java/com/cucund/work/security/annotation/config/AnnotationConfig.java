package com.cucund.work.security.annotation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cucund.work.security.amq.config.ActiveMqConfig;
import com.cucund.work.security.annotation.processor.AnnotationListenerProcessor;
import com.cucund.work.security.annotation.processor.ServerConfig;
import com.cucund.work.security.annotation.provider.Producer;

@Configuration
@Import(ActiveMqConfig.class)
public class AnnotationConfig {

	
	@Bean
	public Producer producer() {
		return new Producer();
	}
	
	@Bean
	public AnnotationListenerProcessor AnnotationListenerProcessor(){
		return new AnnotationListenerProcessor();
	}
	
	@Bean
	public ServerConfig serverConfig(){
		return new ServerConfig();
	}
	
}
