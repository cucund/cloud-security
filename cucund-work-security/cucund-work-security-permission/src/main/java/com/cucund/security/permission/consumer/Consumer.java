package com.cucund.security.permission.consumer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.cucund.security.common.utils.StringUtils;
import com.cucund.security.permission.model.GatewayApiDefine;
import com.cucund.security.permission.model.PermissionList;
import com.cucund.security.permission.service.GatewayApiDefineService;
import com.cucund.security.permission.service.PermissionListService;
import com.cucund.work.security.amq.config.VersionProtocol;

@Component
public class Consumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
	
	@Autowired
	private PermissionListService permissionListService;
	
	@Autowired
	private GatewayApiDefineService gatewayApiDefineService;
	
	@Autowired  
    private DiscoveryClient discoveryClient; 
	
	/**
	 * 处理 扫描到的action
	 * @param msg
	 */
	@JmsListener(destination = "permission.register.queue")
	public void offlineReceive(String msg){
		//根据协议  获取信息
		VersionProtocol protocol = VersionProtocol.newInstance(msg);
		PermissionList permission = protocol.returnBody(PermissionList.class);
		//验证permission类的数据完整性
		String errorMsg = verifyPermission(permission);
		if(errorMsg.equals("")){
			permissionListService.savePermission(permission);
			LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! 注册成功");
			return;
		}
		LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! MSG:"+errorMsg);
	}
	/**
	 * 处理  APP注册
	 * @param msg
	 */
	@JmsListener(destination = "gatewayApi.register.queue")
	public void gatewayApiRegister(String msg){
		//根据协议  获取信息
		VersionProtocol protocol = VersionProtocol.newInstance(msg);
		GatewayApiDefine gatewayApiDefine = protocol.returnBody(GatewayApiDefine.class);
		//验证gatewayApiDefine类的数据完整性
		String errorMsg = verifyGatewayApiDefine(gatewayApiDefine,true);
		if(errorMsg.equals("")){
			gatewayApiDefineService.setGatewayApiDefault(gatewayApiDefine);
			LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! 注册成功");
			return;
		}
		if(errorMsg.equals("update")){//需要更新状态
			GatewayApiDefine update = new GatewayApiDefine();
			update.setServiceId(gatewayApiDefine.getServiceId());
			update.setDataState(1);
			gatewayApiDefineService.updateGatewayApiDefine(update);
		}
		LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! MSG:"+errorMsg);
	}
	/**
	 * 处理  APP离线
	 * @param msg
	 */
	@JmsListener(destination = "gatewayApi.offline.queue")
	public void gatewayApiOffline(String msg){
		//根据协议  获取信息
		System.out.println(msg);
		VersionProtocol protocol = VersionProtocol.newInstance(msg);
		GatewayApiDefine gatewayApiDefine = protocol.returnBody(GatewayApiDefine.class);
		//验证gatewayApiDefine类的数据完整性
		String errorMsg = verifyGatewayApiDefine(gatewayApiDefine,false);
		if(errorMsg.equals("")){
			gatewayApiDefine.setUrl(null);
			gatewayApiDefineService.updateGatewayApiDefine(gatewayApiDefine);
			LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! 已经离线");
			return;
		}
		LOGGER.debug(protocol.getClassName()+":"+protocol.getAppKey()+":"+protocol.getQueueName()+"!! MSG:"+errorMsg);
	}
	
	private String verifyGatewayApiDefine(GatewayApiDefine gatewayApiDefine,boolean online) {
		String errorMsg = "";
		String getServiceId = gatewayApiDefine.getServiceId();
		if(!StringUtils.isNotBlank(getServiceId)){
			errorMsg +="getServiceId为空|";
		}
		if(!StringUtils.isBlank(errorMsg)){
			return errorMsg;
		}
		if(online==true){
			Map<String, Object> pamp = new HashMap<String, Object>();
			pamp.put("fuzzy", true);
			pamp.put("serviceId", getServiceId);
			GatewayApiDefine gatewayApiDefineDB = gatewayApiDefineService.query(pamp);
			if(gatewayApiDefineDB != null){
				if(gatewayApiDefineDB.getDataState()==0){
					return "update";
				};
				return "has";
			}
		}
		if(online==false){
			List<ServiceInstance> instances = discoveryClient.getInstances(gatewayApiDefine.getServiceId());
			if(instances.size()==1){
				int port = instances.get(0).getPort();
				String host = instances.get(0).getHost();
				String url = host+":"+port;
				boolean equals = url.equals(gatewayApiDefine.getUrl());
				return equals?"":"当前serviceId还有在线服务器!";
			}
			if(instances.size()>1){
				return "当前serviceId还有在线服务器!";
			}
		}
		return "";
		
	}

	/**
	 * 验证permission类的数据完整性
	 * @param permission
	 * @return
	 */
	private String verifyPermission(PermissionList permission) {
		String errorMsg = "";
		String action = permission.getPermissionListAction();
		if(!StringUtils.isNotBlank(action)){
			errorMsg +="action为空|";
		}
		String method = permission.getPermissionListMethod();
		if(!StringUtils.isNotBlank(method)){
			errorMsg +="method为空|";
		}
		String appName = permission.getAppmanageIcode();
		if(!StringUtils.isNotBlank(appName)){
			errorMsg +="appName为空|";
		}
		String clazzName = permission.getPermissionListClass();
		if(!StringUtils.isNotBlank(clazzName)){
			errorMsg +="clazzName为空|";
		}
		if(!StringUtils.isBlank(errorMsg)){
			return errorMsg;
		}
		Map<String, Object> pamp = new HashMap<String, Object>();
		pamp.put("fuzzy", true);
		pamp.put("permissionListAction", action);
		pamp.put("permissionListMethod", method);
		pamp.put("appmanageIcode", appName);
		PermissionList permissionList = permissionListService.query(pamp);
		if(permissionList != null){
			return "has";
		}
		return errorMsg;
	}

//	@JmsListener(destination = "start.test.queue")
//	public void onlineReceive(String msg){
//		System.out.println("start.test.queue Msg 接受成功!! 内容:"+msg );
//	}
}