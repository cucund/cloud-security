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
	
	@Value("${spring.application.name}")
	private String appName;
	
	public void sendMQMessage(VersionProtocol protocol) throws JMSException {
		protocol.setAppKey(appName);
		String queueName = protocol.getQueueName();
		if(queueName==null){
			new JMSException("501", "send JMS Message Error,No channel name is set");
		}
		Queue queue = ProducerManager.getQueue(queueName);
		String msg = JSONObject.toJSONString(protocol);
		this.jmsMessagingTemplate.convertAndSend(queue,msg);
	}

	/**
	 * 项目启动时   测试队列是否正常方法
	 */
	public void run(String... strings) throws Exception {
		Queue queue = ProducerManager.getQueue("start.test.queue");
		this.jmsMessagingTemplate.convertAndSend(queue,"启动测试  APP:"+appName+"!!提供方发出");
	}
	
}