package com.huajie.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 隐患整改历史表;
 * @author : http://www.chiner.pro
 * @date : 2023-10-28
 */
@Data
@ApiModel(value = "隐患整改历史表",description = "")
public class ProblemReformHistory implements Serializable{
    /** id */
    @Id
    @ApiModelProperty(name = "id",notes = "")
    private Long id ;
    /** 隐患id */
    @ApiModelProperty(name = "隐患id",notes = "")
    private Long problemId ;
    /** sign, reply, timeout, reform, delayApprove, delayApprovePass, delayApproveReject, reformApprove, reformApprovePass, reformApproveReject */
    @ApiModelProperty(name = "sign, reply, timeout, reform, delayApprove, delayApprovePass, delayApproveReject, reformApprove, reformApprovePass, reformApproveReject",notes = "")
    private String actionType ;
    /** 原始状态 */
    @ApiModelProperty(name = "原始状态",notes = "")
    private String oldState ;
    /** 新状态 */
    @ApiModelProperty(name = "新状态",notes = "")
    private String newState ;
    /** 备注说明 */
    @ApiModelProperty(name = "备注说明",notes = "")
    private String remark ;
    /** 来源 enterprise; goverment */
    @ApiModelProperty(name = "来源 enterprise",notes = " goverment")
    private String source ;
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
    private Date submitTime ;
    /** 图片1 */
    @ApiModelProperty(name = "图片1",notes = "")
    private String problemPic1 ;
    /** 图片2 */
    @ApiModelProperty(name = "图片2",notes = "")
    private String problemPic2 ;
    /** 图片3 */
    @ApiModelProperty(name = "图片3",notes = "")
    private String problemPic3 ;
    /** 图片4 */
    @ApiModelProperty(name = "图片4",notes = "")
    private String problemPic4 ;
    /** 图片5 */
    @ApiModelProperty(name = "图片5",notes = "")
    private String problemPic5 ;
    /** 图片6 */
    @ApiModelProperty(name = "图片6",notes = "")
    private String problemPic6 ;
    /** 图片7 */
    @ApiModelProperty(name = "图片7",notes = "")
    private String problemPic7 ;
    /** 图片8 */
    @ApiModelProperty(name = "图片8",notes = "")
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


