package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Data
public class MenuResponseVO {

    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单url")
    private String menuUrl;

    @ApiModelProperty("父级菜单id")
    private Integer parentId;

}
