package com.huajie.domain.common.enums;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/11/12
 */
public enum  SignStatusEnum {

    NotSign("未签收", Byte.valueOf("0")),

    Sign("已签收", Byte.valueOf("1"));

    private String desc;

    private Byte code;

    public Byte getCode() {
        return code;
    }

    SignStatusEnum(String desc, Byte code){
        this.desc = desc;
        this.code = code;
    }



}
