package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Data
public class EnterpriseRegiestRequestVO {

    @ApiModelProperty("企业名称")
    @NotNull(message = "企业名称不能为空")
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

    @ApiModelProperty("企业省地址")
    @NotNull(message = "企业省地址不能为空")
    @NotBlank(message = "企业省地址不能为空")
    private String province;

    @ApiModelProperty("企业市地址")
    @NotNull(message = "企业市地址不能为空")
    @NotBlank(message = "企业市地址不能为空")
    private String city;

    @ApiModelProperty("企业区/县地址")
    @NotNull(message = "企业区/县地址不能为空")
    @NotBlank(message = "企业区/县地址不能为空")
    private String region;

    @ApiModelProperty("企业街道地址")
    @NotNull(message = "企业街道地址不能为空")
    @NotBlank(message = "企业街道地址不能为空")
    private String street;

    @ApiModelProperty("企业门牌号")
    @NotBlank(message = "企业门牌号不能为空")
    @NotNull(message = "企业门牌号不能为空")
    private String address;

    @ApiModelProperty("企业性质")
    @NotBlank(message = "企业性质不能为空")
    @NotNull(message = "企业性质不能为空")
    private String enterpriseType;

    @ApiModelProperty("行业类别")
    @NotNull(message = "行业类别不能为空")
    @NotBlank(message = "行业类别不能为空")
    private String entIndustryClassification;

    @ApiModelProperty("消防安全管理类别")
    @NotNull(message = "消防安全管理类别不能为空")
    @NotBlank(message = "消防安全管理类别不能为空")
    private String entFireType;

    @ApiModelProperty("企业消防安全责任人")
    @Size(min = 1, message = "企业消防安全责任人 最少有一个")
    private List<UserAddRequestVO> entAdminList;

    @ApiModelProperty("企业消防安全管理人")
    @Size(min = 1, message = "企业消防安全管理人 最少有一个")
    private List<UserAddRequestVO> entOperatorList;

}
