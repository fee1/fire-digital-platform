package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProblemDetailResponseVO {

    @ApiModelProperty(name = "整改回复意见（政府）",notes = "")
    private ProblemReformHistoryResponseVO governmentReformReply;

    @ApiModelProperty(name = "整改回复意见（企业）",notes = "")
    private ProblemReformHistoryResponseVO enterpriseReformReply;

    @ApiModelProperty(name = "整改动作历史",notes = "")
    private List<ProblemReformHistoryResponseVO> reformHistory;


    @ApiModelProperty(name = "id",notes = "")
    private Long id ;
    /** 租戶id */
    @ApiModelProperty(name = "企业租戶id",notes = "")
    private Integer entTenantId ;

    @ApiModelProperty(name = "企业名称",notes = "")
    private String entTenantName;

    @ApiModelProperty(name = "政府租户id",notes = "")
    private Integer govTenantId ;
    /** 隐患状态 submit;wait_reform */
    @ApiModelProperty(name = "隐患状态",notes = "SUBMIT, TODO, TIMEOUT, DELAY_APPROVE, REFORM_APPROVE, FINISH")
    private String state ;
    /** 隐患状态名称 */
    @ApiModelProperty(name = "隐患状态名称",notes = "已提交，待整改，超时未整改，延迟整改审批中，整改审批中，已解决")
    private String stateName ;

    /** 隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查 */
    @ApiModelProperty(name = "隐患类型 patrol 巡查，inspect 检查，selfcheck 综合自查, system 系统定时巡查",notes = "")
    private String problemType ;
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
    /** 提交人头像 */
    @ApiModelProperty(name = "提交人头像",notes = "")
    private String submitUserHeadPic ;
    /** 提交时间 */
    @ApiModelProperty(name = "提交时间",notes = "")
    private Date submitTime ;

    @ApiModelProperty("隐患图片")
    private List<String> problemPicList;

    @Version
    private Integer version;
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
