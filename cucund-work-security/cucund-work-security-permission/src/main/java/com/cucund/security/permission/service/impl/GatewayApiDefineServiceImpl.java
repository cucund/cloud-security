package com.cucund.security.permission.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cucund.security.permission.dao.GatewayApiDefineMapper;
import com.cucund.security.permission.model.GatewayApiDefine;
import com.cucund.security.permission.service.GatewayApiDefineService;

@Service
public class GatewayApiDefineServiceImpl implements GatewayApiDefineService{

	@Autowired
	GatewayApiDefineMapper gatewayApiDefineMapper;
	
	
	
	@Override
	public void saveGatewayApiDefine(GatewayApiDefine gatewayApiDefine) {
		gatewayApiDefineMapper.insertSelective(gatewayApiDefine);
	}
	@Override
	public GatewayApiDefine query(Map<String,Object> map){
		List<GatewayApiDefine> list = gatewayApiDefineMapper.query(map);
		if (list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	

	@Override
	public int updateGatewayApiDefine(GatewayApiDefine gatewayApiDefine) {
		int result = gatewayApiDefineMapper.updateByPrimaryKeySelective(gatewayApiDefine);
		return result;
	}
	
	@Override
	public GatewayApiDefine setGatewayApiDefault(GatewayApiDefine gatewayApiDefine)  {
		//2.默认值
		setDefault(gatewayApiDefine);
		//3.保存
		saveGatewayApiDefine(gatewayApiDefine);
		
		return gatewayApiDefine;
	}
	
	/**
	 * 设置默认值
	 * @param upPermission
	 */
	private void setDefault(GatewayApiDefine gatewayApiDefine){
		if(null==gatewayApiDefine)return;
		String appName = gatewayApiDefine.getServiceId();
		if(null==gatewayApiDefine.getApiName())gatewayApiDefine.setApiName(appName);
		if(null==gatewayApiDefine.getId())gatewayApiDefine.setId(appName);
		if(null==gatewayApiDefine.getPath())gatewayApiDefine.setPath("/"+appName+"/**");
		if(null==gatewayApiDefine.getDataState())gatewayApiDefine.setDataState(1);
		if(null==gatewayApiDefine.getRetryable()){gatewayApiDefine.setRetryable(false);}
		if(null==gatewayApiDefine.getStripPrefix()){gatewayApiDefine.setStripPrefix(1);}
	}
	public String makeMaxCode(int code, int len) {
		StringBuffer codeStr = new StringBuffer(code + "");
		if (codeStr.length() >= len)
			return codeStr.toString();
		int forlen = len - codeStr.length();
		for (int i = 0; i < forlen; i++) {
			codeStr.insert(0, "0");
		}
		return codeStr.toString();
	}

	public String makeMaxCode8(int code) {
		return makeMaxCode(code, 8);
	}

	public String makeMaxCode6(int code) {
		return makeMaxCode(code, 6);
	}
}
