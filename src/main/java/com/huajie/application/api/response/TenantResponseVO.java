package com.huajie.application.api.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/12/5
 */
@Data
public class TenantResponseVO {

    private Integer id;

    @ApiModelProperty("名称")
    private String tenantName;

    @ApiModelProperty("地址")
    private String address;

}
