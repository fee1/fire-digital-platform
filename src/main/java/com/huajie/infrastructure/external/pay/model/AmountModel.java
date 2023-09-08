package com.huajie.infrastructure.external.pay.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/8
 */
@Data
public class AmountModel {

    /**
     * 订单总金额，单位为分。
     */
    private Integer total;

    /**
     * 货币类型
     */
    private String currency = "CNY";

}
