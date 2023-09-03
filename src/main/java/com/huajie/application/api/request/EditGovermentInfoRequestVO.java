package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/3
 */
@Data
public class EditGovermentInfoRequestVO {

    @ApiModelProperty("政府机关名称")
    private String governmentName;

    @ApiModelProperty("省地址")
    private String province;

    @ApiModelProperty("市地址")
    private String city;

    @ApiModelProperty("区/县地址")
    private String region;

    @ApiModelProperty("街道地址")
    private String street;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("政府管理类别 字典value code")
    private String governmentType;

    @ApiModelProperty("政府行业部门 字典value code")
    private String govIndustrySector;

    @ApiModelProperty("消防安全管理类别 字典value code")
    private String entFireType;

    @ApiModelProperty("管理行业类别 字典value code")
    private List<String> entIndustryClassification;

}
