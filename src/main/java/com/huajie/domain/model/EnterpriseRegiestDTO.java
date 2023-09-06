package com.huajie.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuxiaofeng
 * @date 2023/9/6
 */
@Data
public class EnterpriseRegiestDTO {

    @ApiModelProperty("二维码图片地址")
    private String qrcodeUrl;

    @ApiModelProperty("金额")
    private BigDecimal amount;

}
