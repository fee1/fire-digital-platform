package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleCode;

    private String roleName;

    private Integer tenantId;

    private String description;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private static final long serialVersionUID = 1L;

}