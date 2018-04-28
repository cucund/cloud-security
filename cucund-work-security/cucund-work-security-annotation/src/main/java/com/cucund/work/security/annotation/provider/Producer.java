package com.cucund.work.security.annotation.provider;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer implements CommandLineRunner {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@Autowired
	private Queue premissionRegister;
	
	@Autowired
	private Queue startTest;
	
	@Value("${spring.application.name}")
	private String appName;
	
	public void sendpRemissionRegister(String msg){
		this.jmsMessagingTemplate.convertAndSend(this.premissionRegister,msg);
	}
	
	public void sendStartTest(String msg){
		this.jmsMessagingTemplate.convertAndSend(this.startTest,msg);
	}

	/**
	 * 项目启动时   测试队列是否正常方法
	 */
	public void run(String... strings) throws Exception {
		sendStartTest("启动测试  APP:"+appName+"!!提供方发出");
	}
}