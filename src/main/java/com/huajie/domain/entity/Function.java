package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Function implements Serializable {
    private Integer id;

    private String functionCode;

    private String functionName;

    private String api;

    private Boolean limitLogin;

    private Boolean limitAuth;

    private Boolean limitFee;

    private String requestDesc;

    private String responseDesc;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

}