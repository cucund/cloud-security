package com.cucund.work.security.annotation.handler;

import org.springframework.web.bind.annotation.RequestMethod;

public abstract class TemplateHandler implements RestHandler{
	
	
	protected String getMethodRest(RequestMethod[] resultRest) {
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

	protected String arrayGetString(String[] strs){
		StringBuffer sb=new  StringBuffer();
		for(int i=0;i<strs.length;i++){
			if(i != 0){sb.append(",");}
			sb.append(strs[i]);
		}
		String strn=sb.toString();
		return strn;
	}
	
	
	
}
