package com.cucund.work.security.eureka.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cucund.work.security.annotation.ConParameter;
import com.cucund.work.security.annotation.bean.ConParameterEnum;

@RestController  
@RequestMapping("/abc")
public class EurekaClientController {

	
	@Autowired
    private DiscoveryClient discoveryClient;  
      
    @RequestMapping("/")
    @ResponseBody
    String home() {  
        return "Hello World!";  
    }
      
    @SuppressWarnings("deprecation")
    @ConParameter(ConParameterEnum.ALL)
	@RequestMapping(value = "/registered" ,method=RequestMethod.GET)
    public String getRegistered(){
        List<ServiceInstance> list = discoveryClient.getInstances("STORES");  
        System.out.println(discoveryClient.getLocalServiceInstance());  
        System.out.println("discoveryClient.getServices().size() = " + discoveryClient.getServices().size());  
        for( String s :  discoveryClient.getServices()){
            System.out.println("services " + s);
            List<ServiceInstance> serviceInstances =  discoveryClient.getInstances(s);  
            for(ServiceInstance si : serviceInstances){  
                System.out.println("    services:" + s + ":getHost()=" + si.getHost());  
                System.out.println("    services:" + s + ":getPort()=" + si.getPort());  
                System.out.println("    services:" + s + ":getServiceId()=" + si.getServiceId());  
                System.out.println("    services:" + s + ":getUri()=" + si.getUri());  
                System.out.println("    services:" + s + ":getMetadata()=" + si.getMetadata());  
            }  
              
        }  
          
        System.out.println(list.size());  
        if (list != null && list.size() > 0 ) {  
            System.out.println( list.get(0).getUri()  );  
        }  
        return null;  
    }  
}
