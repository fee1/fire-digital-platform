package com.huajie.application.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/10
 */
@Data
public class QrcodeImageResponseVO {

    @ApiModelProperty("二维码图片地址")
    private String qrcodeUrl;

    @ApiModelProperty("订单号")
    private String orderId;

    @ApiModelProperty("金额")
    private String amount;

}
