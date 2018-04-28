package com.cucund.work.security.annotation.processor;

import java.lang.reflect.Method;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cucund.work.security.annotation.bean.RequestMap;
import com.cucund.work.security.annotation.provider.Producer;


@Component
public class AnnotationListenerProcessor implements BeanPostProcessor {
	
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${app.package}")
	private String loadPackage;
	
	@Autowired
	private Producer producer;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * 初始化bean类
	 * 初始化时  读取自定义 注解解析
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		String name = bean.getClass().getName();
		if(!(StringUtils.isNotBlank(loadPackage)&&name.contains(loadPackage))){
			return bean;
		}
		Controller controller = AnnotationUtils.findAnnotation(bean.getClass(), Controller.class);
		if(null ==controller){
			return bean;
		}
		RestController restController = AnnotationUtils.findAnnotation(bean.getClass(), RestController.class);
		if(null ==restController){
			return bean;
		}
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
		if (methods != null) {
			RequestMapping classAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), RequestMapping.class);
			String clazzPath = "";
			if(null!=classAnnotation){
				String[] path = classAnnotation.value();
				clazzPath = getUrl(path);
			}
			for (Method method : methods) {
				RequestMap mapping = null;
				RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
				if (null != requestMapping) {
					mapping = getMapping(method);
				}
				GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
				if (null != getMapping) {
					mapping = getMapping4Get(method);
				}
				PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
				if (null != postMapping) {
					mapping = getMapping4Post(method);
				}
				if(null!=mapping){
					mapping.setClazzName(name);
					mapping.setClazzPath(clazzPath);
					mapping.setAppName(appName);
					producer.sendpRemissionRegister(mapping.toString());
				}
			}
		}
		return bean;
	}

	private RequestMap getMapping4Post(Method method) {
		GetMapping result = AnnotationUtils.findAnnotation(method, GetMapping.class);
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		RequestMap requestMap = new RequestMap();
		requestMap.setPath(path);
		requestMap.setName(name);
		requestMap.setMethod("GET");
		return requestMap;
	}

	private RequestMap getMapping4Get(Method method) {
		PostMapping result = AnnotationUtils.findAnnotation(method, PostMapping.class);
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		RequestMap requestMap = new RequestMap();
		requestMap.setPath(path);
		requestMap.setName(name);
		requestMap.setMethod("POST");
		return requestMap;
	}

	private RequestMap getMapping(Method method){
		RequestMapping result = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		//插入到数据中
		String name= result.name();
		RequestMethod[] resultRest = result.method();
		RequestMap requestMap = new RequestMap();
		requestMap.setPath(getUrl(result.value()));
		requestMap.setName(name);
		String methodStr = getMethodRest(resultRest);
		requestMap.setMethod(methodStr);
		return requestMap;
	}

	private String getMethodRest(RequestMethod[] resultRest) {
		String methodStr = "";
		if(resultRest.length==0){
			methodStr = "*";
		}else{
			String[] strs = new String[resultRest.length];
			for (int i = 0; i < resultRest.length; i++) {
				RequestMethod requestMethod = resultRest[i];
				strs[0] = requestMethod.name();
				methodStr = arrayGetString(strs);
			}
		}
		return methodStr;
	}
	
	private String getUrl(String[] url){
		StringBuffer sb=new  StringBuffer();
		if(url.length == 0){
			return "";
		}
		String path = url[0];
		if(path.equals("/")){
			return "/";
		}
		String[] split = path.split("/");
		for(int i=0;i<split.length;i++){
			String item = split[i];
			if(!StringUtils.isNotBlank(item)){
				continue;
			}
			sb.append("/");
			if(item.indexOf("{")==0&&item.indexOf("}")==item.length()-1){
				sb.append("*");
				continue;
			}
			sb.append(item);
		}
		String strn=sb.toString();
		return strn;
	}
	
	private String arrayGetString(String[] strs){
		StringBuffer sb=new  StringBuffer();
		for(int i=0;i<strs.length;i++){
			if(i != 0){sb.append(",");}
			sb.append(strs[i]);
		}
		String strn=sb.toString();
		return strn;
	}
}