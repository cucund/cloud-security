package com.cucund.work.security.eureka.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cucund.work.security.eureka.config.EurekaConfig;
import com.cucund.work.security.eureka.utils.EurekaServerUtils;
import com.netflix.appinfo.InstanceInfo;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Eureka事件监听
 *
 * @author yinjihuan
 * @create 2018-03-09 13:45
 **/
@Component
public class EurekaStateChangeListener extends EurekaServerUtils{

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaStateChangeListener.class);

    
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
    	LOGGER.info(event.getServerId() + "\t" + event.getAppName() + " 服务下线");
    	JSONObject json = (JSONObject) JSONObject.toJSON(event);
//        request(getLogOutUrl(event),json.toJSONString());
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        LOGGER.info(instanceInfo.getAppName() + "进行注册");
        JSONObject json = (JSONObject) JSONObject.toJSON(instanceInfo);
//        request(getRegisterUrl(event),json.toJSONString());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
    	InstanceInfo instanceInfo = event.getInstanceInfo();
    	LOGGER.info(instanceInfo.getAppName() + " 服务进行续约");
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
    	LOGGER.info("注册中心 启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
    	LOGGER.info("Eureka Server 启动");
    }
    
    
    
}