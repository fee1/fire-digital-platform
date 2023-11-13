package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/10/28
 */
@Data
public class NoticeDetailResponseVO {

    private Integer id;

    private Integer fromUserId;

    private Integer fromTenantId;

    @ApiModelProperty("类型  0: 通知 / 1:通告")
    private Byte type;

    @ApiModelProperty("收件人类型：0： 企业 / 1 ： 政府机构")
    private Byte receiveType;

    @ApiModelProperty("状态, 0: 未发布; 1: 已发布; 2:已过期")
    private Byte status;

    @ApiModelProperty("指定角色，0：表示全部")
    private String roleName;

    @ApiModelProperty("0: 全部 / 1: 指定单位（单选框）")
    private Byte specifyRange;

    private String tenantIds;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("正文")
    private String text;

    @ApiModelProperty("附件")
    private String appendix;

    @ApiModelProperty("通知保留时间(天)")
    private Integer saveDays;

    @ApiModelProperty("过期时间")
    private Date expireTime;

}
