package com.cucund.security.common.plugin;

public class ServerProConfig {
	private String dao;
	private String daoName;
	private String name;//MODEL名称
	private String domainName;//domain后缀名称 有的有Bean有的没有，默认有
	private String ldao;//第一个字母小写
	private String lname;//第一个字母小写MODEL名称
	private String nick;//简称
	private String idName;//系统主键
	private String codeName;//业务主键
	private String uCodeName;//第一个字母大写业务主键
	public String getuCodeName() {
		return uCodeName;
	}
	public void setuCodeName(String uCodeName) {
		this.uCodeName = uCodeName;
	}
	private String getidName;
	private String pageflag;//0 分页 1无分页
	
	public String getCodeName() {
		return codeName;
	}
	//首字母转大写
    public String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	public void setCodeName(String codeName) {
		setuCodeName(toUpperCaseFirstOne(codeName));
		this.codeName = codeName;
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
	public String getGetidName() {
		return getidName;
	}
	public void setGetidName(String getidName) {
		this.getidName = getidName;
	}
	public String getDaoName() {
		return daoName;
	}
	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}
	public String getLdao() {
		return ldao;
	}
	public void setLdao(String ldao) {
		this.ldao = ldao;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getDao() {
		return dao;
	}
	public void setDao(String dao) {
		this.dao = dao;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPageflag() {
		return pageflag;
	}
	public void setPageflag(String pageflag) {
		this.pageflag = pageflag;
	}
}
