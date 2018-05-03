package com.cucund.security.common.plugin;

public class ColumnBean {
	private String simpleTypeClass;
	private String name;
	private String doc;
	private String getterName;
	private String setterName;
	public String getSimpleTypeClass() {
		return simpleTypeClass;
	}
	public void setSimpleTypeClass(String simpleTypeClass) {
		this.simpleTypeClass = simpleTypeClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getGetterName() {
		return getterName;
	}
	public void setGetterName(String getterName) {
		this.getterName = getterName;
	}
	public String getSetterName() {
		return setterName;
	}
	public void setSetterName(String setterName) {
		this.setterName = setterName;
	}
}
