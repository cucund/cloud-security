package com.cucund.work.security.eureka.comfig;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:eureka.properties"})
@EnableEurekaClient
public class EurekaConfig {

}
