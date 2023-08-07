package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class RoleDetailRequestVO {

    @ApiModelProperty("角色id")
    @NotNull(message = "id不能为空")
    private Integer id;

}
