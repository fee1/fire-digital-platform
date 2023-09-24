package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/9/24
 */
@Data
public class PhoneBindingRequestVO extends AppRequestVO {

    @ApiModelProperty("openId")
    @NotNull(message = "openId不能为空")
    @NotBlank(message = "openId不能为空")
    private String openId;


}
