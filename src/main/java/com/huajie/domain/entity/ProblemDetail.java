package com.huajie.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProblemDetail implements Serializable {

    @ApiModelProperty(name = "id",notes = "")
    private Integer id ;
    /** 租戶id */
    @ApiModelProperty(name = "租戶id",notes = "")
    private Integer tenantId ;
    /** 隐患状态 submit;wait_reform */
    @ApiModelProperty(name = "隐患状态 submit",notes = "wait_reform")
    private String state ;
    /** 隐患来源 ent;gov */
    @ApiModelProperty(name = "隐患来源 ent",notes = "gov")
    private String problemSource ;
    /** 隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查 */
    @ApiModelProperty(name = "隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查",notes = "")
    private String porblemType ;
    /** 关联id */
    @ApiModelProperty(name = "关联id",notes = "")
    private Integer relationId ;
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
    /** 问题描述 */
    @ApiModelProperty(name = "问题描述",notes = "")
    private String problemDesc ;
    /** 预整改方案 */
    @ApiModelProperty(name = "预整改方案",notes = "")
    private String preReformDesc ;
    /** 整改超时时间 */
    @ApiModelProperty(name = "整改超时时间",notes = "")
    private Date reformTimeoutTime ;
    /** 提交人id */
    @ApiModelProperty(name = "提交人id",notes = "")
    private Integer submitUserId ;
    /** 提交人 */
    @ApiModelProperty(name = "提交人",notes = "")
    private String submitUserName ;
    /** 提交人联系方式 */
    @ApiModelProperty(name = "提交人联系方式",notes = "")
    private String submitUserPhone ;
    /** 提交时间 */
    @ApiModelProperty(name = "提交时间",notes = "")
    private Date submitItme ;
    /** 隐患图片1 */
    @ApiModelProperty(name = "隐患图片1",notes = "")
    private String problemPic1 ;
    /** 隐患图片2 */
    @ApiModelProperty(name = "隐患图片2",notes = "")
    private String problemPic2 ;
    /** 隐患图片3 */
    @ApiModelProperty(name = "隐患图片3",notes = "")
    private String problemPic3 ;
    /** 隐患图片4 */
    @ApiModelProperty(name = "隐患图片4",notes = "")
    private String problemPic4 ;
    /** 隐患图片5 */
    @ApiModelProperty(name = "隐患图片5",notes = "")
    private String problemPic5 ;
    /** 隐患图片6 */
    @ApiModelProperty(name = "隐患图片6",notes = "")
    private String problemPic6 ;
    /** 隐患图片7 */
    @ApiModelProperty(name = "隐患图片7",notes = "")
    private String problemPic7 ;
    /** 隐患图片8 */
    @ApiModelProperty(name = "隐患图片8",notes = "")
    private String problemPic8 ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createTime ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    private String createUser ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateTime ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    private String updateUser ;

}
