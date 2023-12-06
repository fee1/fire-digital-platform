package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/11/13
 */
@Data
public class PcNoticeResponseVO {

    private Integer id;

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

    @ApiModelProperty("有无附件, true: 存在")
    private Boolean existAppendix;

    @ApiModelProperty("签收状态 0: 未签收， 1：已签收")
    private Integer status;

}
