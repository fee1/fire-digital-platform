package com.huajie.infrastructure.external.sms.model;

import lombok.Data;

@Data
public class ZTResponseBody {

    /**
     * 200 提交成功
     */
    private Integer code;

    /**
     *
     */
    private String msg;

    /**
     *
     */
    private String msgId;

    /**
     * 发了几条
     */
    private Integer contNum;

}
