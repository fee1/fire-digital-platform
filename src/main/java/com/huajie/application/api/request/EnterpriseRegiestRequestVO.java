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
    @NotNull(message = "企业名称 不能为空")
    @NotBlank(message = "企业名称 不能为空")
    private String enterpriseName;

    @ApiModelProperty("省地址")
    @NotNull(message = "省地址 不能为空")
    private Integer provinceId;

    @ApiModelProperty("市地址")
    @NotNull(message = "市地址 不能为空")
    private Integer cityId;

    @ApiModelProperty("区/县地址")
    @NotNull(message = "区/县地址 不能为空")
    private Integer regionId;

    @ApiModelProperty("街道地址")
    @NotNull(message = "街道地址 不能为空")
    private Integer streetId;

    @ApiModelProperty("门牌号")
    @NotBlank(message = "门牌号 不能为空")
    @NotNull(message = "门牌号 不能为空")
    private String address;

    @ApiModelProperty("企业性质 字典value code")
    @NotBlank(message = "企业性质 不能为空")
    @NotNull(message = "企业性质 不能为空")
    private String enterpriseType;

    @ApiModelProperty("行业类别 字典value code")
    @NotNull(message = "行业类别 不能为空")
    @NotBlank(message = "行业类别 不能为空")
    private String entIndustryClassification;

    @ApiModelProperty("消防安全管理类别 字典value code")
    @NotNull(message = "消防安全管理类别 不能为空")
    @NotBlank(message = "消防安全管理类别 不能为空")
    private String entFireType;

    @ApiModelProperty("企业消防安全责任人")
    @Size(min = 1, message = "企业消防安全责任人 最少有一个")
    private List<UserAddRequestVO> entAdminList;

    @ApiModelProperty("企业消防安全管理人")
    @Size(min = 1, message = "企业消防安全管理人 最少有一个")
    private List<UserAddRequestVO> entOperatorList;

}
