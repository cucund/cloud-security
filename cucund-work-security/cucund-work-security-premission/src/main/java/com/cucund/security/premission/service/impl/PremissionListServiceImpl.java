package com.cucund.security.premission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cucund.security.premission.dao.PremissionListMapper;
import com.cucund.security.premission.model.PremissionList;
import com.cucund.security.premission.service.PremissionListService;

@Service
public class PremissionListServiceImpl implements PremissionListService{

	@Autowired
	PremissionListMapper premissionListMapper;
	
	@Override
	public void savePremissionList(PremissionList premissionList) {
		premissionListMapper.insertSelective(premissionList);
	}

	
	
}
