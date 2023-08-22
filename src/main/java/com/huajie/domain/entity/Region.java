package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Region implements Serializable {
    private Integer id;

    private String regionName;

    private String regionLevel;

    private Integer parentId;

    private String governmentName;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private static final long serialVersionUID = 1L;

}