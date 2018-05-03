package com.cucund.security.common.plugin;

import java.util.List;

public class ServerConfig {
	private String constants;
	private String mn;
	private String interfaceStr;
	private String linterfaceStr;//第一个字母小写
	private String implementsStr;
	private String serverPkg;
	private List<String> importslist;
	private List<String> importsilist;
	private List<ServerProConfig> proList;
	private String getsysdateDao;
	private String name;
	private String nick;
	private String description;
	private String model;
	public String getMn() {
		return mn;
	}
	public void setMn(String mn) {
		this.mn = mn;
	}
	public List<String> getImportsilist() {
		return importsilist;
	}
	public void setImportsilist(List<String> importsilist) {
		this.importsilist = importsilist;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLinterfaceStr() {
		return linterfaceStr;
	}
	public void setLinterfaceStr(String linterfaceStr) {
		this.linterfaceStr = linterfaceStr;
	}
	public String getGetsysdateDao() {
		return getsysdateDao;
	}
	public void setGetsysdateDao(String getsysdateDao) {
		this.getsysdateDao = getsysdateDao;
	}
	public String getServerPkg() {
		return serverPkg;
	}
	public void setServerPkg(String serverPkg) {
		this.serverPkg = serverPkg;
	}
	public List<String> getImportslist() {
		return importslist;
	}
	public void setImportslist(List<String> importslist) {
		this.importslist = importslist;
	}
	public String getConstants() {
		return constants;
	}
	public void setConstants(String constants) {
		this.constants = constants;
	}
	public String getInterfaceStr() {
		return interfaceStr;
	}
	public void setInterfaceStr(String interfaceStr) {
		this.interfaceStr = interfaceStr;
	}
	public String getImplementsStr() {
		return implementsStr;
	}
	public void setImplementsStr(String implementsStr) {
		this.implementsStr = implementsStr;
	}
	public List<ServerProConfig> getProList() {
		return proList;
	}
	public void setProList(List<ServerProConfig> proList) {
		this.proList = proList;
	}
}
