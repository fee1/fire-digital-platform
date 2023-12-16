package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Notice implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fromUserId;

    private Integer fromTenantId;

    private Byte type;

    private Byte receiveType;

    private Byte status;

    private String roleName;

    private Byte specifyRange;

    private String tenantIds;

    private String title;

    private String text;

    private String appendix;

    private Integer saveDays;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

}