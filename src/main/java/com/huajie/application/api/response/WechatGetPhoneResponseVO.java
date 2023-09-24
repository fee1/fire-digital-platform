package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/18
 */
@Data
public class WechatGetPhoneResponseVO {

    @ApiModelProperty("手机号")
    private String phone;

}
