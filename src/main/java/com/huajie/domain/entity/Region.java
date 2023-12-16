package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

    private static final long serialVersionUID = 1L;

}