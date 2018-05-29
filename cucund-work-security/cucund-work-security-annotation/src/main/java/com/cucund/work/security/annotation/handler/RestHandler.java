package com.cucund.work.security.annotation.handler;

import java.lang.reflect.Method;

import com.cucund.work.security.annotation.bean.PermissionList;

public interface RestHandler {

	PermissionList execute(Method method);
	
}
