package com.huajie.application.api.response;

import com.huajie.application.api.request.UserAddRequestVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/9/10
 */
@Data
public class GovermentInfoResponseVO {


    @ApiModelProperty("政府机关名称")
    private String governmentName;

    private Integer provinceId;

    @ApiModelProperty("省地址")
    private Integer provinceName;

    private Integer cityId;

    @ApiModelProperty("市地址")
    private Integer cityName;

    private Integer regionId;

    @ApiModelProperty("区/县地址")
    private Integer regionName;

    private Integer streetId;

    @ApiModelProperty("街道地址")
    private Integer streetName;

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

    @ApiModelProperty("政府消防安全责任人")
    private List<UserDetailResponseVO> govAdminList;

    @ApiModelProperty("政府消防安全管理人")
    private List<UserDetailResponseVO> govOperatorList;

}
