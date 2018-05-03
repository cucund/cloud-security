package com.cucund.security.premission.model;

import java.util.Date;

public class PremissionList {
    private Integer permissionListId;

    /**
     * 代码
     */
    private String permissionListCode;

    /**
     * 系统权限代码
     */
    private String permissionCode;

    /**
     * app code
     */
    private String appmanageIcode;

    private String permissionListClass;

    /**
     * action
     */
    private String permissionListAction;

    /**
     * method
     */
    private String permissionListMethod;

    /**
     * 类别： 0：操作员权限判断    1：判断是否登陆(SYSPOP_CODE=-2) 2：公共权限不用判断(SYSPOP_CODE=-1)             
     */
    private Integer permissionListSort;

    /**
     * 类型 ： 0：正常 
     */
    private Integer permissionListType;

    /**
     * 功能名称
     */
    private String permissionListName;

    /**
     * 标志 ： 0：不记录 1 记录
     */
    private Integer permissionLogStart;

    /**
     * 标志 ： 0：不记录 1 记录
     */
    private Integer permissionLogEnd;

    /**
     * 业务号码解析 a.b.c
     */
    private String permissionLogSno;

    /**
     * 业务号码解析 a.b.c
     */
    private String permissionLogNno;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 备注
     */
    private String memo;

    /**
     * 状态
     */
    private Integer dataState;

    private Integer permissionListFlag;

    private String tenantCode;

    /**
     * 是否需要缓存
     */
    private Integer permissionListCache;

    private Integer permissionListAuthLogin;

    public Integer getPermissionListId() {
        return permissionListId;
    }

    public void setPermissionListId(Integer permissionListId) {
        this.permissionListId = permissionListId;
    }

    public String getPermissionListCode() {
        return permissionListCode;
    }

    public void setPermissionListCode(String permissionListCode) {
        this.permissionListCode = permissionListCode == null ? null : permissionListCode.trim();
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode == null ? null : permissionCode.trim();
    }

    public String getAppmanageIcode() {
        return appmanageIcode;
    }

    public void setAppmanageIcode(String appmanageIcode) {
        this.appmanageIcode = appmanageIcode == null ? null : appmanageIcode.trim();
    }

    public String getPermissionListClass() {
        return permissionListClass;
    }

    public void setPermissionListClass(String permissionListClass) {
        this.permissionListClass = permissionListClass == null ? null : permissionListClass.trim();
    }

    public String getPermissionListAction() {
        return permissionListAction;
    }

    public void setPermissionListAction(String permissionListAction) {
        this.permissionListAction = permissionListAction == null ? null : permissionListAction.trim();
    }

    public String getPermissionListMethod() {
        return permissionListMethod;
    }

    public void setPermissionListMethod(String permissionListMethod) {
        this.permissionListMethod = permissionListMethod == null ? null : permissionListMethod.trim();
    }

    public Integer getPermissionListSort() {
        return permissionListSort;
    }

    public void setPermissionListSort(Integer permissionListSort) {
        this.permissionListSort = permissionListSort;
    }

    public Integer getPermissionListType() {
        return permissionListType;
    }

    public void setPermissionListType(Integer permissionListType) {
        this.permissionListType = permissionListType;
    }

    public String getPermissionListName() {
        return permissionListName;
    }

    public void setPermissionListName(String permissionListName) {
        this.permissionListName = permissionListName == null ? null : permissionListName.trim();
    }

    public Integer getPermissionLogStart() {
        return permissionLogStart;
    }

    public void setPermissionLogStart(Integer permissionLogStart) {
        this.permissionLogStart = permissionLogStart;
    }

    public Integer getPermissionLogEnd() {
        return permissionLogEnd;
    }

    public void setPermissionLogEnd(Integer permissionLogEnd) {
        this.permissionLogEnd = permissionLogEnd;
    }

    public String getPermissionLogSno() {
        return permissionLogSno;
    }

    public void setPermissionLogSno(String permissionLogSno) {
        this.permissionLogSno = permissionLogSno == null ? null : permissionLogSno.trim();
    }

    public String getPermissionLogNno() {
        return permissionLogNno;
    }

    public void setPermissionLogNno(String permissionLogNno) {
        this.permissionLogNno = permissionLogNno == null ? null : permissionLogNno.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Integer getDataState() {
        return dataState;
    }

    public void setDataState(Integer dataState) {
        this.dataState = dataState;
    }

    public Integer getPermissionListFlag() {
        return permissionListFlag;
    }

    public void setPermissionListFlag(Integer permissionListFlag) {
        this.permissionListFlag = permissionListFlag;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode == null ? null : tenantCode.trim();
    }

    public Integer getPermissionListCache() {
        return permissionListCache;
    }

    public void setPermissionListCache(Integer permissionListCache) {
        this.permissionListCache = permissionListCache;
    }

    public Integer getPermissionListAuthLogin() {
        return permissionListAuthLogin;
    }

    public void setPermissionListAuthLogin(Integer permissionListAuthLogin) {
        this.permissionListAuthLogin = permissionListAuthLogin;
    }
}