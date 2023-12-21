package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
public class ProblemReformHistoryResponseVO {

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
    /** 动作名称 */
    @ApiModelProperty(name = "动作名称",notes = "")
    private String actionName ;

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
    /** 来源 企业：企业责任人/企业管理员，政府：政府机构名称 */
    @ApiModelProperty(name = "来源租户",notes = "企业：企业责任人/企业管理员，政府：政府机构名称")
    private String sourceTenant ;
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
    /** 图片1 */
    @ApiModelProperty(name = "图片1",notes = "")
    private List<String> problemPicList ;
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
