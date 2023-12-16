package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

    private String headPic;

    private static final long serialVersionUID = 1L;
}