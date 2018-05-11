package com.cucund.work.security.annotation.processor;

import java.lang.reflect.Method;

import javax.jms.JMSException;

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

import com.alibaba.fastjson.JSONObject;
import com.cucund.work.security.amq.config.VersionProtocol;
import com.cucund.work.security.annotation.ConParameter;
import com.cucund.work.security.annotation.bean.ConParameterEnum;
import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.provider.Producer;

/**
 * 监听注解启动
 * @author cucund
 */
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
		String className = bean.getClass().getName();
		if(!(StringUtils.isNotBlank(loadPackage)&&className.contains(loadPackage))){
			return bean;
		}
		Controller controller = AnnotationUtils.findAnnotation(bean.getClass(), Controller.class);
		if(null ==controller){//类上是否被注解Controller 修饰
			return bean;
		}
		RestController restController = AnnotationUtils.findAnnotation(bean.getClass(), RestController.class);
		if(null ==restController){//类上是否被注解RestController 修饰
			return bean;
		}
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
		if (methods != null) {
			RequestMapping classAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), RequestMapping.class);
			String clazzPath = "";
			if(null!=classAnnotation){//类上是否被注解RequestMapping 修饰
				String[] path = classAnnotation.value();
				clazzPath = getUrl(path);
			}
			for (Method method : methods) {
				PermissionList permission = null;
				RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
				if (null != requestMapping) {//方法上是否被注解RequestMapping 修饰
					permission = getMapping(requestMapping);
				}
				GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
				if (null != getMapping) {//方法上是否被注解GetMapping 修饰
					permission = getMapping4Get(getMapping);
				}
				PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
				if (null != postMapping) {//方法上是否被注解PostMapping 修饰
					permission = getMapping4Post(postMapping);
				}
				if(null!=permission){//获得的内容是否为空   ,其他rest风格请自行添加
					permission.setPermissionListClass(className);
					permission.setPermissionListAction(clazzPath+permission.getPermissionListAction());
					permission.setAppmanageIcode(appName);
					getConParameterAnnotataion(permission,method);//获取当前方法下是否有ConParameter注解
					//初始化协议
					String json = JSONObject.toJSONString(permission);
					VersionProtocol protocol = new VersionProtocol("permission.register.queue");
					protocol.setClassName(className);
					protocol.setMsgBody(json);
					try {
						producer.sendMQMessage(protocol);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bean;
	}
	/**
	 * 根据自定义的注释取出里面的数据,更新permission
	 * @param permission
	 * @param method
	 */
	private void getConParameterAnnotataion(PermissionList permission, Method method) {
		ConParameter result = AnnotationUtils.findAnnotation(method, ConParameter.class);
		if(null!=result){
			ConParameterEnum value = result.value();
			Integer index = value.getIndex();
			permission.setPermissionListSort(index);
			Integer authLogin = null;
			if(!result.authLogin().equals("") ){
				authLogin = Integer.valueOf(result.authLogin());
			}
			permission.setPermissionListAuthLogin(authLogin);
		}else{
			permission.setPermissionListSort(ConParameterEnum.ALL.getIndex());
			permission.setPermissionListAuthLogin(null);
		}
		permission.setPermissionListType(0);
		permission.setDataState(1);
	}

	private PermissionList getMapping4Post(PostMapping result) {
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(path);
		requestMap.setPermissionListName(name);
		requestMap.setPermissionListMethod("GET");
		return requestMap;
	}

	private PermissionList getMapping4Get(GetMapping result) {
		//插入到数据中
		String name= result.name();
		String path= arrayGetString(result.value());
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(path);
		requestMap.setPermissionListName(name);
		requestMap.setPermissionListMethod("POST");
		return requestMap;
	}

	private PermissionList getMapping(RequestMapping result){
		//插入到数据中
		String name= result.name();
		RequestMethod[] resultRest = result.method();
		PermissionList requestMap = new PermissionList();
		requestMap.setPermissionListAction(getUrl(result.value()));
		requestMap.setPermissionListName(name);
		String methodStr = getMethodRest(resultRest);
		requestMap.setPermissionListMethod(methodStr);
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