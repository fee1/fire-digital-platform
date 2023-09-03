package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class EnterpriseVerifyRequestVO {

    @ApiModelProperty("企业ID")
    @NotNull(message = "企业ID 不能为空")
    private Integer enterpriseId;



}
