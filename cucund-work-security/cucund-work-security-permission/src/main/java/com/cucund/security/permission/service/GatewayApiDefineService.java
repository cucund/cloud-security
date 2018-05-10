package com.cucund.security.permission.service;

import java.util.Map;

import com.cucund.security.permission.model.GatewayApiDefine;

public interface GatewayApiDefineService {

	void saveGatewayApiDefine(GatewayApiDefine gatewayApiDefine);

	GatewayApiDefine query(Map<String, Object> map);

	GatewayApiDefine setGatewayApiDefault(GatewayApiDefine gatewayApiDefine);

	int updateGatewayApiDefine(GatewayApiDefine gatewayApiDefine);
}
