package com.huajie.application.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/27
 */
@Data
public class GovermentRegiestRequestVO {

    @ApiModelProperty("政府机关名称")
    @NotBlank(message = "政府机关名称 不能为空")
    @NotNull(message = "政府机关名称 不能为空")
    private String governmentName;

    @ApiModelProperty("省地址")
    @NotNull(message = "省地址 不能为空")
    private Integer provinceId;

    @ApiModelProperty("市地址")
    @NotNull(message = "市地址 不能为空")
    private Integer cityId;

    @ApiModelProperty("区/县地址")
//    @NotNull(message = "区/县地址 不能为空")
    private Integer regionId;

    @ApiModelProperty("街道地址")
//    @NotNull(message = "街道地址 不能为空")
    private Integer streetId;

    @ApiModelProperty("地址")
    @NotBlank(message = "地址 不能为空")
    @NotNull(message = "地址 不能为空")
    private String address;

    @ApiModelProperty("政府管理类别 字典value code")
    @NotBlank(message = "政府管理类别 不能为空")
    @NotNull(message = "政府管理类别 不能为空")
    private String governmentType;

    @ApiModelProperty("政府行业部门 字典value code")
//    @NotBlank(message = "政府行业部门 不能为空")
//    @NotNull(message = "政府行业部门 不能为空")
    private String govIndustrySector;

    @ApiModelProperty("消防安全管理类别 字典value code")
    @NotBlank(message = "消防安全管理类别 不能为空")
    @NotNull(message = "消防安全管理类别 不能为空")
    private String entFireType;

    @ApiModelProperty("管理行业类别 字典value code")
    private List<String> entIndustryClassification;

    @ApiModelProperty("政府消防安全责任人")
    @Size(min = 1, message = "政府消防安全责任人 最少有一个")
    @Valid
    private List<UserAddRequestVO> govAdminList;

    @ApiModelProperty("政府消防安全管理人")
    @Size(min = 1, message = "政府消防安全管理人 最少有一个")
    @Valid
    private List<UserAddRequestVO> govOperatorList;

}
