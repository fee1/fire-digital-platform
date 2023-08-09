package com.huajie.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class Function implements Serializable {
    private Integer id;

    private String functionCode;

    private String functionName;

    private String api;

    private Byte limitLogin;

    private Byte limitAuth;

    private Byte limitFee;

    private String requestDesc;

    private String responseDesc;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Byte getLimitLogin() {
        return limitLogin;
    }

    public void setLimitLogin(Byte limitLogin) {
        this.limitLogin = limitLogin;
    }

    public Byte getLimitAuth() {
        return limitAuth;
    }

    public void setLimitAuth(Byte limitAuth) {
        this.limitAuth = limitAuth;
    }

    public Byte getLimitFee() {
        return limitFee;
    }

    public void setLimitFee(Byte limitFee) {
        this.limitFee = limitFee;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", functionCode=").append(functionCode);
        sb.append(", functionName=").append(functionName);
        sb.append(", api=").append(api);
        sb.append(", limitLogin=").append(limitLogin);
        sb.append(", limitAuth=").append(limitAuth);
        sb.append(", limitFee=").append(limitFee);
        sb.append(", requestDesc=").append(requestDesc);
        sb.append(", responseDesc=").append(responseDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}