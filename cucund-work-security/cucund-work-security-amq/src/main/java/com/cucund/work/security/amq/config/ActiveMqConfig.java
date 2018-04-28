package com.cucund.work.security.amq.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMqConfig {
	
	@Value("${spring.activemq.user}")
	private String username;
	@Value("${spring.activemq.password}")
	private String password;
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	
	// 加载YML格式自定义配置文件  
    @Bean  
    public static PropertySourcesPlaceholderConfigurer properties() {  
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();  
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();  
        yaml.setResources(new ClassPathResource("amq.yml"));//class引入  
        configurer.setProperties(yaml.getObject());  
        return configurer;  
    }
	
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory (){
		ActiveMQConnectionFactory activeMQConnectionFactory =
				new ActiveMQConnectionFactory(username,password,brokerUrl);
		return activeMQConnectionFactory;
	}
}