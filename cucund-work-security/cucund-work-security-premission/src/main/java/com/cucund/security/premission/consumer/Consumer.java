package com.cucund.security.premission.consumer;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	
	@JmsListener(destination = "premission.register.queue")
	public void offlineReceive(String msg){
		System.out.println("premission.register.queue Msg 接受成功!! 内容:"+msg  );
	}
	
	@JmsListener(destination = "start.test.queue")
	public void onlineReceive(String msg){
		System.out.println("start.test.queue Msg 接受成功!! 内容:"+msg );
	}
	
}