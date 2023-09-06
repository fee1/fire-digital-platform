package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Place implements Serializable {

    private Integer id;

    private Integer tenantId;

    private String nfcCode;

    private String placeName;

    private String placeAddress;

    private Integer operatorId;

    private String remark;

    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

    @Version
    private Integer version;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
}
