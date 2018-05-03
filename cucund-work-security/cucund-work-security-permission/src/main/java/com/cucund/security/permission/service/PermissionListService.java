package com.cucund.security.permission.service;

import java.util.Map;

import com.cucund.security.permission.model.PermissionList;

public interface PermissionListService {
	
	public void savePermissionList(PermissionList permissionList);
	
	public PermissionList query(Map<String, Object> map);

	PermissionList savePermission(PermissionList permissionList);

}
