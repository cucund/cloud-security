package com.cucund.work.security.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/activemq")
public class AmqController {

	
	@Autowired
	RestTemplate restTemplate;
	
    @RequestMapping(value = "/sendOffline")
    @ResponseBody
    public String sendOffline(@RequestParam String msg){
        return restTemplate.getForObject("http://AMQ-SERVER/activemq/sendOffline?msg="+msg,String.class);
    }
    
    @RequestMapping(value = "/sendOnline")
    @ResponseBody
    public String sendOnline(@RequestParam String msg){
    	return restTemplate.getForObject("http://AMQ-SERVER/activemq/sendOnline?msg="+msg,String.class);
    }
}
