package com.huajie.domain.common.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author zhuxiaofeng
 * @date 2023/12/12
 */
@Getter
public enum  IsDeleteEnum {

    DELETE(1, "已删除"),
    UN_DELETE(0, "未删除");

    private Integer code;

    private String desc;

    IsDeleteEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }


}
