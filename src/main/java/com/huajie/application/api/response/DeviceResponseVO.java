package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeviceResponseVO {

    private Integer id;

    @ApiModelProperty("租户id")
    private Integer tenantId;

    @ApiModelProperty("点位id")
    private Integer placeId;

    @ApiModelProperty("设备类型 代码")
    private String deviceType;

    @ApiModelProperty("设备类型描述")
    private String deviceTypeDesc;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceNo;

    @ApiModelProperty("出厂日期")
    private Date productionDate;

    @ApiModelProperty("最近一次使用日期")
    private Date lastUseDate;

    @ApiModelProperty("最近一次充装日期")
    private Date lastReplaceDate;

    @ApiModelProperty("灭火器类型")
    private String extinguisherType;

    @ApiModelProperty("灭火器类型描述")
    private String extinguisherTypeDesc;

    @ApiModelProperty("电源类型")
    private String powerType;

    @ApiModelProperty("电源类型描述")
    private String powerTypeDesc;

    @ApiModelProperty("操作人id")
    private Integer operatorId;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("操作人联系方式")
    private String operatorPhone;

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

    private List<InspectDetailResponseVO> inspectDetailList;
}
