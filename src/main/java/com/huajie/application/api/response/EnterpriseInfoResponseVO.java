package com.huajie.application.api.response;

import com.huajie.application.api.request.UserAddRequestVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/10/23
 */
@Data
public class EnterpriseInfoResponseVO {

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("省地址")
    private String provinceName;

    private Integer cityCode;

    @ApiModelProperty("市地址")
    private String cityName;

    private Integer regionCode;

    @ApiModelProperty("区/县地址")
    private String regionName;

    private Integer streetCode;

    @ApiModelProperty("街道地址")
    private String streetName;

    @ApiModelProperty("门牌号")
    private String address;

    private String enterpriseType;

    @ApiModelProperty("企业性质 字典value code")
    private String enterpriseTypeName;

    private String entIndustryClassification;

    @ApiModelProperty("行业类别 字典value code")
    private String entIndustryClassificationName;

    private String entFireType;

    @ApiModelProperty("消防安全管理类别 字典value code")
    private String entFireTypeName;

    @ApiModelProperty("企业消防安全责任人")
    private List<EnterpriseUserInfoResponseVO> entAdminList;

    @ApiModelProperty("企业消防安全管理人")
    private List<EnterpriseUserInfoResponseVO> entOperatorList;

}
