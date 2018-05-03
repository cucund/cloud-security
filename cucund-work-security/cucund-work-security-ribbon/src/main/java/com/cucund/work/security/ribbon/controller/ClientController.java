package com.cucund.work.security.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cucund.work.security.annotation.ConParameter;
import com.cucund.work.security.annotation.bean.ConParameterEnum;

@RestController
@RequestMapping("/client")
public class ClientController {

	
	@Autowired
	RestTemplate restTemplate;
	
    @RequestMapping(value = "/test/{aaaa}")
    @ResponseBody
    public String test(@RequestParam String msg){
        return restTemplate.getForObject("http://CLIENT-SERVER/abc/",String.class);
    }
    
    @RequestMapping(value = "/registered")
    @ResponseBody
    @ConParameter(ConParameterEnum.ALL)
    public String registered(@RequestParam String msg){
    	return restTemplate.getForObject("http://CLIENT-SERVER/abc/registered",String.class);
    }
}
