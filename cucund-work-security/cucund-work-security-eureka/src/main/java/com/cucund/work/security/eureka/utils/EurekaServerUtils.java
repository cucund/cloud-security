package com.cucund.work.security.eureka.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EurekaServerUtils {

    @Autowired
    private String amqServer;
    @Autowired
    private String ribbonServer;
    @Autowired
    private String amqOffpath;
    @Autowired
    private String amqOnpath;
    @Autowired
    private String ribbonOffpath;
    @Autowired
    private String ribbonOnpath;
    
    private static boolean ribbonOn = false;
    
    private static String ribbonAddress = "";
    private static List<String> amqAddress = new ArrayList<String>() ;

    /**
     * 注册服务
     * @param url 注册服务的地址
     * @param msg 注册服务的内容
     */
    public void request(String url,String msg){
    	try {
	    	if(url == null){
	    		new Exception("已经没有可以注册服务");
	    	}
	    	OkHttpClient okHttpClient = new OkHttpClient();
	        //创建RequestBody对象，将参数按照指定的MediaType封装
	    	 FormBody formBody = new FormBody
	                 .Builder()
	                 .add("msg",msg)//设置参数名称和参数值
	                 .build();
	        Request request = new Request
	                .Builder()
	                .post(formBody)//Post请求的参数传递
	                .url(url)
	                .build();
            Response response = okHttpClient.newCall(request).execute();
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取 注销服务地址
     * @param event
     * @return
     */
    public String getLogOutUrl(EurekaInstanceCanceledEvent event){
    	String appName = event.getAppName();
    	if(appName.equals(amqServer)){
    		String serverId = event.getServerId();
    		for (String address : amqAddress) {
    			String ip = address.split(":")[0];
    			if(serverId.equals(ip)){
    				amqAddress.remove(address);
    			}
			}
    	}else if(appName.equals(ribbonServer)){
    		ribbonOn = false;
    		ribbonAddress = "";
    	}
		if(amqAddress.size()==0){
			return null;
		}
    	if(ribbonOn){
    		return "http://"+ribbonAddress+ribbonOffpath;
    	}
    	return "http://"+amqAddress.get(0)+amqOffpath;
    }
    
    /**
     * 获取 注册服务地址
     * @param event
     * @return
     */
    public String getRegisterUrl(EurekaInstanceRegisteredEvent event){
    	String appName = event.getInstanceInfo().getAppName();
    	if(appName.equals(amqServer)){
    		String address = event.getInstanceInfo().getIPAddr()+":"+event.getInstanceInfo().getPort();
    		amqAddress.add(address);
    	}else if(appName.equals(ribbonServer)){
    		ribbonOn = true;
    		ribbonAddress = event.getInstanceInfo().getIPAddr()+":"+event.getInstanceInfo().getPort();
    		System.out.println(ribbonAddress);
    	}
    	if(amqAddress.size()==0){
			return null;
		}
    	if(ribbonOn){
    		return "http://"+ribbonAddress+ribbonOnpath;
    	}
    	return "http://"+amqAddress.get(0)+amqOnpath;
    }
}
