package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Data
public class DicValueResponseVO {

    private Integer id;

    @ApiModelProperty("自定义序号，排序用")
    private Integer valueNo;

    @ApiModelProperty("值列表代码全局唯一")
    private String valueCode;

    @ApiModelProperty("值名称")
    private String valueName;

}
