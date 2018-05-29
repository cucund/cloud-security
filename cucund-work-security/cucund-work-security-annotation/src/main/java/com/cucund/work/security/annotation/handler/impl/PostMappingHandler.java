package com.cucund.work.security.annotation.handler.impl;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.handler.TemplateHandler;

public class PostMappingHandler extends TemplateHandler {

	@Override
	public PermissionList execute(Method method) {

		PermissionList permission = null;
		GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
		if (null != getMapping) {//方法上是否被注解GetMapping 修饰
			permission = getMapping(getMapping);
		}
		return permission;
	}

	private PermissionList getMapping(GetMapping result) {
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(path);
		requestMap.setPermissionListName(name);
		requestMap.setPermissionListMethod("GET");
		return requestMap;
	}
}
