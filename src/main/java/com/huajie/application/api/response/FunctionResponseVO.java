package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Data
public class FunctionResponseVO {

    private Integer id;

    @ApiModelProperty("功能code全局唯一")
    private String functionCode;

    @ApiModelProperty("功能名称")
    private String functionName;

    @ApiModelProperty("url")
    private String api;

    @ApiModelProperty("是否限制登录态")
    private Byte limitLogin;

    @ApiModelProperty("是否限制权限")
    private Byte limitAuth;

    @ApiModelProperty("是否限制收费")
    private Byte limitFee;

    @ApiModelProperty("请求描述")
    private String requestDesc;

    @ApiModelProperty("响应描述")
    private String responseDesc;

}
