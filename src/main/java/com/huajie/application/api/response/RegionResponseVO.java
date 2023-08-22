package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/22
 */
@Data
public class RegionResponseVO {

    private Integer id;

    @ApiModelProperty("行政区名称")
    private String regionName;

    @ApiModelProperty("行政区级别;1:省,2:市,3:区/县,4:街道")
    private String regionLevel;

    @ApiModelProperty("上级行政区id")
    private Integer parentId;

    @ApiModelProperty("政府机构名称")
    private String governmentName;

}
