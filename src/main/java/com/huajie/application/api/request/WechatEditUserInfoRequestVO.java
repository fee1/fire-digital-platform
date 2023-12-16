package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/12/16
 */
@Data
public class WechatEditUserInfoRequestVO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String headPic;

}
