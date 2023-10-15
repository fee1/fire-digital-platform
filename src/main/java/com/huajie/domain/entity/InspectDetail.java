package com.huajie.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InspectDetail implements Serializable {


    @ApiModelProperty(name = "id",notes = "")
    private Integer id ;
    /** 企业租戶id */
    @ApiModelProperty(name = "企业租戶id",notes = "")
    private Integer entTenantId ;
    /** 政府租戶id */
    @ApiModelProperty(name = "政府租戶id",notes = "")
    private Integer govTenantId ;
    /** 隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查 */
    @ApiModelProperty(name = "隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查",notes = "")
    private String inspectType ;
    /** 检查结果 pass/error */
    @ApiModelProperty(name = "检查结果 pass/error",notes = "")
    private String inspectResult ;
    /** 检查明细 检查项+结果拼接 */
    @ApiModelProperty(name = "检查明细 检查项+结果拼接",notes = "")
    private String inspectDetail ;
    /** 点位id */
    @ApiModelProperty(name = "点位id",notes = "")
    private Integer placeId ;
    /** 点位名称 */
    @ApiModelProperty(name = "点位名称",notes = "")
    private String placeName ;
    /** 点位地址 */
    @ApiModelProperty(name = "点位地址",notes = "")
    private String placeAddress ;
    /** 设备id */
    @ApiModelProperty(name = "设备id",notes = "")
    private Integer deviceId ;
    /** 设备名称 */
    @ApiModelProperty(name = "设备名称",notes = "")
    private String deviceName ;
    /** 提交人id */
    @ApiModelProperty(name = "提交人id",notes = "")
    private Integer submitUserId ;
    /** 提交人 */
    @ApiModelProperty(name = "提交人",notes = "")
    private String submitUserName ;
    /** 提交人联系方式 */
    @ApiModelProperty(name = "提交人联系方式",notes = "")
    private String submitUserPhone ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private String updateUser ;

}
