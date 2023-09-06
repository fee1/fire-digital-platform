package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlaceResponseVO {

    private Integer id;

    @ApiModelProperty("租户id")
    private Integer tenantId;

    @ApiModelProperty("nfcCode")
    private String nfcCode;

    @ApiModelProperty("点位名称")
    private String placeName;

    @ApiModelProperty("点位地址")
    private String placeAddress;

    @ApiModelProperty("操作员id")
    private Integer operatorId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("删除标识")
    private Integer deleted;

    @ApiModelProperty("版本号")
    private Integer version;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

}
