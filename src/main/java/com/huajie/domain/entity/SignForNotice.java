package com.huajie.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SignForNotice implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer noticeId;

    private Byte signStatus;

    private Date signTime;

    private Integer sendUserId;

    private Date sendTime;

    private Integer isDelete;

}