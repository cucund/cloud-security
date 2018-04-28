package com.cucund.work.security.annotation.bean;


public class RequestMap {
	
	private String appName;
	private String clazzName;
	private String method;
	private String path;
	private String clazzPath;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getClazzName() {
		return clazzName;
	}
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	public String getClazzPath() {
		return clazzPath;
	}
	public void setClazzPath(String clazzPath) {
		this.clazzPath = clazzPath;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	@Override
	public String toString() {
		return "RequestMap [appName=" + appName + ", clazzName=" + clazzName + ", method=" + method + ", path=" + path
				+ ", clazzPath=" + clazzPath + ", name=" + name + "]";
	}
	
}
