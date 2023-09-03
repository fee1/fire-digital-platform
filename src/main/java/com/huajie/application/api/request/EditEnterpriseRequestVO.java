package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class EditEnterpriseRequestVO {

    @ApiModelProperty("企业id")
    private Integer enterpriseId;

    @ApiModelProperty("行业类别")
    private String entIndustryClassification;

}
