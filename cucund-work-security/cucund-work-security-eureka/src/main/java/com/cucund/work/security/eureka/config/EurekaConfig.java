package com.cucund.work.security.eureka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaConfig {

	
    
    
    @Value("${amq-server.name}")
    private String amqServer;
    
    @Value("${ribbon-server.name}")
    private String ribbonServer;
    
    @Value("${ribbon-server.offpath}")
    private String ribbonOffpath;
    
    @Value("${amq-server.onpath}")
    private String ribbonOnpath;
    
    @Value("${amq-server.offpath}")
    private String amqOffpath;
    
    @Value("${ribbon-server.onpath}")
    private String amqOnpath;
    
    @Bean
    public String ribbonOffpath(){
    	return ribbonOffpath;
    }
    
    @Bean
    public String ribbonOnpath(){
    	return ribbonOnpath;
    }
    
    @Bean
    public String amqOffpath(){
    	return amqOffpath;
    }
    
    @Bean
    public String amqOnpath(){
    	return amqOnpath;
    }
    
    @Bean
    public String ribbonServer(){
    	return ribbonServer.toUpperCase();
    }
    
    @Bean
    public String amqServer(){
    	return amqServer.toUpperCase();
    }
    
}
