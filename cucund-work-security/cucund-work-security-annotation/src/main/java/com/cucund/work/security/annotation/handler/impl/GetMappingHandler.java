package com.cucund.work.security.annotation.handler.impl;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PostMapping;

import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.handler.TemplateHandler;

public class GetMappingHandler extends TemplateHandler {

	@Override
	public PermissionList execute(Method method) {

		PermissionList permission = null;
		PostMapping getMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
		if (null != getMapping) {//方法上是否被注解GetMapping 修饰
			permission = getMapping(getMapping);
		}
		return permission;
	}

	private PermissionList getMapping(PostMapping result) {
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(path);
		requestMap.setPermissionListName(name);
		requestMap.setPermissionListMethod("POST");
		return requestMap;
	}
}
