package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoleMenuRelation implements Serializable {
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private Date createTime;

    private String createUser;

    private static final long serialVersionUID = 1L;

}