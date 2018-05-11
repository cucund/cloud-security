package com.cucund.security.permission.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cucund.security.common.utils.StringUtils;
import com.cucund.security.permission.dao.PermissionListMapper;
import com.cucund.security.permission.model.PermissionList;
import com.cucund.security.permission.service.PermissionListService;

@Service
public class PermissionListServiceImpl implements PermissionListService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionListServiceImpl.class);
	@Autowired
	PermissionListMapper permissionListMapper;
	
	
	@Override
	public void savePermissionList(PermissionList permissionList) {
		permissionListMapper.insertSelective(permissionList);
	}
	
	@Override
	public PermissionList query(Map<String,Object> map){
		List<PermissionList> list = permissionListMapper.query(map);
		if (list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public PermissionList savePermission(PermissionList permissionList)  {
		//2.默认值
		setDefault(permissionList);
		//3.保存
		savePermissionList(permissionList);
		
		return permissionList;
	}
	
	/**
	 * 设置默认值
	 * @param upPermission
	 */
	private void setDefault(PermissionList permissionList){
		if(null==permissionList)return;
		if(null==permissionList.getDataState())permissionList.setDataState(0);
		if(null==permissionList.getGmtCreate())permissionList.setGmtCreate(getSysDate());
		if(StringUtils.isBlank(permissionList.getPermissionListCode())) {
			int code = getMaxCodeList();
			permissionList.setPermissionListCode(makeMaxCode8(code));
		}
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

	//计数
	private Integer code = null;
	
	private synchronized int getMaxCodeList() {
		if(code==null){
			try {
				int num = permissionListMapper.getMaxCode();
				code = num;
			} catch (Exception e) {
				code = 0;
			}
		}
		LOGGER.info("code:"+code);
		return ++code;
	}
	/**
	 * 获取系统时间
	 * @return
	 */
	private Date getSysDate(){
		try{
			return permissionListMapper.selectSysDate();
		}catch(Exception e){
			
		}
		return null;
	}
}
