package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class RoleAddRequestVO {

    @ApiModelProperty("角色编码")
    @NotNull(message = "角色编码不能为空")
    @NotBlank(message = "角色编码不能为空")
    public String roleCode;

    @ApiModelProperty("角色名称")
    @NotNull(message = "角色名称不能为空")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty("角色含义解释")
    @NotNull(message = "角色含义解释不能为空")
    @NotBlank(message = "角色含义解释不能为空")
    private String description;

    @ApiModelProperty("分配的菜单")
    @NotNull(message = "分配的菜单不能为空")
    private List<Integer> menuIds;

}
