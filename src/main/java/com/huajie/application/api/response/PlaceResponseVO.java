package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;;

    @TableField(value = "update_user",fill = FieldFill.UPDATE)
    private String updateUser;

    @TableField(exist = false)
    private Integer deviceCount;

}
