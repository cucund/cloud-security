package com.cucund.work.security.annotation.provider;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cucund.work.security.amq.config.VersionProtocol;

@Component
public class Producer implements CommandLineRunner {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@Autowired
	private Queue permissionRegister;
	
	@Autowired
	private Queue startTest;
	
	@Autowired
	private Queue gatewayApiRegister;
	
	@Autowired
	private Queue gatewayApiOffline;
	
	@Value("${spring.application.name}")
	private String appName;
	
	public void sendPermissionRegister(VersionProtocol protocol) throws JMSException{
		protocol.setQueueName(this.permissionRegister.getQueueName());
		String msg = JSONObject.toJSONString(protocol);
		this.jmsMessagingTemplate.convertAndSend(this.permissionRegister,msg);
	}
	
	public void sendGatewayApiRegister(VersionProtocol protocol) throws JMSException{
		protocol.setQueueName(this.gatewayApiRegister.getQueueName());
		String msg = JSONObject.toJSONString(protocol);
		this.jmsMessagingTemplate.convertAndSend(this.gatewayApiRegister,msg);
	}
	
	public void sendGatewayApiOffline(VersionProtocol protocol) throws JMSException {
		protocol.setQueueName(this.gatewayApiOffline.getQueueName());
		String msg = JSONObject.toJSONString(protocol);
		this.jmsMessagingTemplate.convertAndSend(this.gatewayApiOffline,msg);
	}

	/**
	 * 项目启动时   测试队列是否正常方法
	 */
	public void run(String... strings) throws Exception {
		sendStartTest("启动测试  APP:"+appName+"!!提供方发出");
	}
	
	public void sendStartTest(String msg){
		this.jmsMessagingTemplate.convertAndSend(this.startTest,msg);
	}
	
}