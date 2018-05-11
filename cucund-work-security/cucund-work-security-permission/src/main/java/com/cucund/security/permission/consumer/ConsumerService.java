package com.cucund.security.permission.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public interface ConsumerService {

	public void onlineReceive(String msg);
}
