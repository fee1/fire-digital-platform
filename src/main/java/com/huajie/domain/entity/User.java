package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private Integer id;

    private String userNo;

    private Integer tenantId;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private Integer roleId;

    private String openId;

    private String unionId;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private String HeadPic;

    private static final long serialVersionUID = 1L;
}