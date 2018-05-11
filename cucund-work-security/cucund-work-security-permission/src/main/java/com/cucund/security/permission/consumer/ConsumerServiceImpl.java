package com.cucund.security.permission.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerServiceImpl implements ConsumerService {
	
	@JmsListener(destination = "start.test.queue")
	public void onlineReceive(String msg){
		System.out.println("start.test.queue Msg 接受成功!! 内容:"+msg );
	}

}
