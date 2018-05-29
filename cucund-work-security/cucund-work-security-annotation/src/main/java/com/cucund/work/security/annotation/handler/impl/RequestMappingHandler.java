package com.cucund.work.security.annotation.handler.impl;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cucund.security.common.utils.HttpUtils;
import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.handler.TemplateHandler;

public class RequestMappingHandler extends TemplateHandler {

	@Override
	public PermissionList execute(Method method) {

		PermissionList permission = null;
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (null != requestMapping) {//方法上是否被注解RequestMapping 修饰
			permission = getMapping(requestMapping);
		}
		return permission;
	}

	private PermissionList getMapping(RequestMapping result){
		//插入到数据中
		String name= result.name();
		RequestMethod[] resultRest = result.method();
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(HttpUtils.getUrl(result.value()));
		requestMap.setPermissionListName(name);
		String methodStr = getMethodRest(resultRest);
		requestMap.setPermissionListMethod(methodStr);
		return requestMap;
	}
}
