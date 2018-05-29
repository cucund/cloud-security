package com.cucund.security.common.utils;


public class HttpUtils {
	
	public static String getUrl(String[] url){
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

}
