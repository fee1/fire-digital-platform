package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/11/13
 */
@Data
public class NoticeAppDetailResponseVO {

    @ApiModelProperty("发送日期")
    private Date sendTime;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("发送人")
    private String sendUserName;

    @ApiModelProperty("发送人来源单位")
    private String fromTenantName;

    @ApiModelProperty("内容（正文缩略）")
    private String text;

    @ApiModelProperty("发送人手机号")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

}
