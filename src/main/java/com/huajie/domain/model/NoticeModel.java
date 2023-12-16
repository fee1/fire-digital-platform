package com.huajie.domain.model;

import com.huajie.domain.entity.Notice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuxiaofeng
 * @date 2023/12/5
 */
@Data
public class NoticeModel extends Notice {

    private Integer signStatus;

    @ApiModelProperty("发送人")
    private String sendUserName;

    @ApiModelProperty("发送人头像")
    private String headPic;

    private String phone;

    private Date sendTime;

}
