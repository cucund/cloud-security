package com.cucund.work.security.annotation.provider;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

public class ProducerManager {
	
	private static Map<String, Queue> queueMap = new HashMap<String, Queue>();
	
	public static Queue getQueue(String queueName){
		Queue queue1 = queueMap.get(queueName);
		if(queue1==null){
			synchronized (ProducerManager.class) {
				Queue queue2 = queueMap.get(queueName);
				if(queue2==null){
					Queue amqQueue = new ActiveMQQueue(queueName);
					queueMap.put(queueName, amqQueue);
					return amqQueue;
				}
				return queue2;
			}
		}
		return queue1;
	}

}
