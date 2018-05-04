package com.cucund.work.security.amq.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
@PropertySource({"classpath:amq.properties"}) 
public class ActiveMqConfig {
	
	@Value("${spring.activemq.user}")
	private String username;
	@Value("${spring.activemq.password}")
	private String password;
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory (){
		ActiveMQConnectionFactory activeMQConnectionFactory =
				new ActiveMQConnectionFactory(username,password,brokerUrl);
		return activeMQConnectionFactory;
	}
}