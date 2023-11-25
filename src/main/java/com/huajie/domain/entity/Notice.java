package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    private Integer sendUserId;

//    private String sendUserName;

    private Date sendTime;

    private Byte type;

    private Byte receiveType;

    private Byte status;

    private Integer roleId;

    private Byte specifyRange;

    private String tenantIds;

    private String title;

    private String text;

    private String appendix;

    private Integer saveDays;

    private Date expireTime;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

}