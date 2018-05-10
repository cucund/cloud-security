package com.cucund.work.security.annotation.processor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cucund.work.security.amq.config.VersionProtocol;
import com.cucund.work.security.annotation.bean.GatewayApiDefine;
import com.cucund.work.security.annotation.provider.Producer;

@Component
public class ServerConfig {
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${server.port}")
	private String port;
	
	private static String ip;
	
	@Autowired
	private Producer producer;
	
	@Autowired
    private DiscoveryClient discoveryClient;  
	
	@PostConstruct
	public void initIt() throws Exception {
		GatewayApiDefine gatewayApiDefine = new GatewayApiDefine();
		gatewayApiDefine.setServiceId(appName);
		String json = JSONObject.toJSONString(gatewayApiDefine);
		VersionProtocol protocol = new VersionProtocol();
		protocol.setAppKey(appName);
		protocol.setClassName(ServerConfig.class.getName());
		protocol.setMsgBody(json);
		producer.sendGatewayApiRegister(protocol);
		ServiceInstance local = discoveryClient.getLocalServiceInstance();
		ip = local.getHost();
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
		GatewayApiDefine gatewayApiDefine = new GatewayApiDefine();
		gatewayApiDefine.setServiceId(appName);
		gatewayApiDefine.setUrl(ip+":"+port);//查询离线时是否在 service实例中
		gatewayApiDefine.setDataState(0);
		String json = JSONObject.toJSONString(gatewayApiDefine);
		VersionProtocol protocol = new VersionProtocol();
		protocol.setAppKey(appName);
		protocol.setClassName(ServerConfig.class.getName());
		protocol.setMsgBody(json);
		producer.sendGatewayApiOffline(protocol);
	}

}
