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
import com.cucund.security.common.utils.HttpUtils;
import com.cucund.work.security.amq.config.VersionProtocol;
import com.cucund.work.security.annotation.ConParameter;
import com.cucund.work.security.annotation.bean.ConParameterEnum;
import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.chain.RestMappingHandlerExecutionChain;
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
		RestController restController = AnnotationUtils.findAnnotation(bean.getClass(), RestController.class);
		if(null ==restController||null == controller){//类上是否被注解RestController或者Controller 修饰
			return bean;
		}
		send(bean, className);
		return bean;
	}

	private void send(Object bean, String className) {
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
		if (methods != null) {
			RequestMapping classAnnotation = AnnotationUtils.findAnnotation(bean.getClass(), RequestMapping.class);
			String clazzPath = "";
			if(null!=classAnnotation){//类上是否被注解RequestMapping 修饰
				String[] path = classAnnotation.value();
				clazzPath = HttpUtils.getUrl(path);
			}
			for (Method method : methods) {
				PermissionList permission = makePermissionList(method);
				if(null!=permission){//获得的内容是否为空   ,其他rest风格请自行添加
					permission.setPermissionListClass(className);
					permission.setPermissionListAction(clazzPath+permission.getPermissionListAction());
					permission.setAppmanageIcode(appName);
					getConParameterAnnotataion(permission,method);//获取当前方法下是否有ConParameter注解
					String json = JSONObject.toJSONString(permission);
					
					//初始化协议
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

	
    public PermissionList makePermissionList(Method method) {
        
        if (method == null) {
            return null;
        }
        
        return  RestMappingHandlerExecutionChain.newInstance().execute(method);
    }
}