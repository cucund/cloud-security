package com.cucund.work.security.annotation.bean;

import java.util.Date;

public class PermissionList {
    private Integer permissionListId;

    private String permissionListCode;

    private String permissionCode;

    private String appmanageIcode;

    private String permissionListClass;

    private String permissionListAction;

    private String permissionListMethod;

    private Integer permissionListSort;

    private Integer permissionListType;

    private String permissionListName;

    private Integer permissionLogStart;

    private Integer permissionLogEnd;

    private String permissionLogSno;

    private String permissionLogNno;

    private Date gmtCreate;

    private Date gmtModified;

    private String memo;

    private Integer dataState;

    private Integer permissionListFlag;

    private String tenantCode;

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