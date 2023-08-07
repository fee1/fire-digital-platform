package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class RoleAddRequestVO {

    @ApiModelProperty("角色编码")
    public String roleCode;

}
