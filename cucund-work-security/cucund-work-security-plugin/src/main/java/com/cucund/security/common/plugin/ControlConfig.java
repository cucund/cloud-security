package com.cucund.security.common.plugin;

import java.util.List;
import java.util.Map;

public class ControlConfig {
	private String pagename;//html包名
	private String servicePagename;//包名
	private String projectPath;//项目的绝对路径
	private String listJspath;//权限明细级别设置JSPATH,不填使用CONTEXT，如有主表和明细，生成两个CONTROL，但权限是一个，context是两个，这个时候需要在明细级别设置
	private String context;//小写
	private String upContext;//大写
	private String mm;//ap
	private String model;//接口@ApiMethod注解中code中第二级定义：plugin.${Model}.updateFchannelState  例如：adaptation
	private String modelName;//MODEL名称 第一个字母大写  例如：PluginAdaptation
	private String nameNick;//业务对应的简称，一般去掉MODEL名称前面的Plugin  第一个字母大写  例如：Adaptation
	private String domainName;//domain后缀，有的是Domain,有的是DomainBean,默认是DomainBean 例如：Domain
	private String reDomainName;//redomain后缀，有的是ReDomain,有的是ReDomainBean,默认是ReDomainBean 例如：ReDomain
	private String idName;//业务对象对应的ID名称 例如：adaptationId
	private String name;//功能名称 例如： 适配器
	private List<String> columnList;//domain对象的属性列表
	private Map<String,String> columnNameMap;//domain对象的属性的注解,用于做为html字段名称
	private String lnameNick;//业务对应的简称，一般去掉MODEL名称前面的  第一个字母小写 ,由系统根据nameNick生成 例如：adaptation
	private String lmodelName;//MODEL名称 第一个字母小写 由系统根据modelName生成 例如：pluginAdaptation
	private List<String> queryList;//查询属性名
	private String str1="#";//特殊符号 # 
	private String str2="$";//特殊符号 $ 
	private String str3="??";//特殊符号 ??
	private String str4="@";//特殊符号 @
	private List<ColumnBean> infieldList;
	private List<ColumnBean> refieldList;

	public String getListJspath() {
		return listJspath;
	}
	public void setListJspath(String listJspath) {
		this.listJspath = listJspath;
	}
	public String getServicePagename() {
		return servicePagename;
	}
	public void setServicePagename(String servicePagename) {
		this.servicePagename = servicePagename;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public String getUpContext() {
		setUpContext(toUpperCaseFirstOne(getContext()));
		return upContext;
	}
	public List<ColumnBean> getInfieldList() {
		return infieldList;
	}
	public void setInfieldList(List<ColumnBean> infieldList) {
		this.infieldList = infieldList;
	}
	public List<ColumnBean> getRefieldList() {
		return refieldList;
	}
	public void setRefieldList(List<ColumnBean> refieldList) {
		this.refieldList = refieldList;
	}
	public void setUpContext(String upContext) {
		this.upContext = upContext;
	}
	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getReDomainName() {
		if(null==reDomainName||"".equals(reDomainName))setReDomainName("ReDomainBean");
		return reDomainName;
	}
	public void setReDomainName(String reDomainName) {
		this.reDomainName = reDomainName;
	}
	public List<String> getQueryList() {
		return queryList;
	}
	public void setQueryList(List<String> queryList) {
		this.queryList = queryList;
	}
	public String getStr4() {
		return str4;
	}
	public void setStr4(String str4) {
		this.str4 = str4;
	}
	public String getStr3() {
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getLmodelName() {
		return lmodelName;
	}
	public void setLmodelName(String lmodelName) {
		this.lmodelName = lmodelName;
	}
	public String getLnameNick() {
		return lnameNick;
	}
	public void setLnameNick(String lnameNick) {
		this.lnameNick = lnameNick;
	}
	public List<String> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}
	
	public Map<String, String> getColumnNameMap() {
		return columnNameMap;
	}
	public void setColumnNameMap(Map<String, String> columnNameMap) {
		this.columnNameMap = columnNameMap;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		setLmodelName(toLowerCaseFirstOne(modelName));
		this.modelName = modelName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getNameNick() {
		return nameNick;
	}
	public void setNameNick(String nameNick) {
		setLnameNick(toLowerCaseFirstOne(nameNick));
		this.nameNick = nameNick;
	}
	public String getDomainName() {
		if(null==domainName||"".equals(domainName))setDomainName("DomainBean");
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//首字母转大写
    public String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
  //首字母转小写
    public String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
