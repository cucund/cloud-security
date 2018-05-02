package com.cucund.work.security.annotation.bean;

public enum ConParameterEnum {
	
	MASTER(0, "操作员权限判断"), LOGIN(1,"判断是否登陆"), ALL(2,"公共权限不用判断"); 
    // 成员变量  
    private String name;  
    private Integer code;  
    // 构造方法  
    private ConParameterEnum(Integer code, String name) {  
        this.name = name;  
        this.code = code;  
    }  
    // 普通方法  
    public static String getName(Integer code) {  
        for (ConParameterEnum c : ConParameterEnum.values()) {  
            if (c.getIndex() == code) {  
                return c.name;  
            }  
        }  
        return null;  
    }
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    
    public Integer getIndex() {  
        return code;  
    }  

}
