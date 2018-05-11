package com.cucund.work.security.amq.config;

import com.alibaba.fastjson.JSONObject;
import com.cucund.security.common.utils.StringUtils;

public class VersionProtocol {
	
	private String appKey;
	
	private String className;
	
	private String queueName;
	
	private String msgBody;

	/**
	 * 初始化结束获取msgBody信息,并转成 clazz的类型
	 * @param clazz
	 * @return
	 */
	public <T> T returnBody(Class<T> clazz) {
		return (T) JSONObject.parseObject(msgBody,clazz);
	}
	
	/**
	 * 通过json字符串进行初始化
	 * @param msg
	 * @return
	 */
	public static VersionProtocol newInstance(String msg) {
		VersionProtocol newObj = JSONObject.parseObject(msg, VersionProtocol.class);
		if(newObj==null)return null;
		String errorMsg = "";
		if(StringUtils.isBlank(newObj.getAppKey())){
			errorMsg +="协议异常:没有appkey|";
		}
		if(StringUtils.isBlank(newObj.getQueueName())){
			errorMsg +="协议异常:没有queueName|";
		}
		if(StringUtils.isBlank(newObj.getClassName())){
			errorMsg +="协议异常:没有className|";
		}
		if(StringUtils.isNotBlank(errorMsg)){
			throw new ProtocolException(errorMsg);
		}
		return newObj;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public VersionProtocol() {
		
	}

	public VersionProtocol(String queueName) {
		this.queueName = queueName;
	}


	
}
