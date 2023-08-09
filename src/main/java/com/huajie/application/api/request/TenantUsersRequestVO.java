package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class TenantUsersRequestVO {

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Integer pageNum;
    /**
     * 每页数量
     */
    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("用户账号")
    private String userNo;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户名")
    private String userName;

}
