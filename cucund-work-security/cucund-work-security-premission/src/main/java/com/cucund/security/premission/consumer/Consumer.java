package com.cucund.security.premission.consumer;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cucund.security.premission.model.PremissionList;

@Component
public class Consumer {
	
	/**
	 * 如何处理 扫描到的action
	 * @param msg
	 */
	@JmsListener(destination = "premission.register.queue")
	public void offlineReceive(String msg){
		PremissionList premission = JSONObject.parseObject(msg, PremissionList.class);
		System.out.println("premission.register.queue Msg 接受成功!! 内容:"+premission.toString()  );
	}
	
	@JmsListener(destination = "start.test.queue")
	public void onlineReceive(String msg){
		System.out.println("start.test.queue Msg 接受成功!! 内容:"+msg );
	}
	
}