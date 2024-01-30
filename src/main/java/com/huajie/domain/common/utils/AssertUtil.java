package com.huajie.domain.common.utils;

import com.huajie.domain.common.exception.ServerException;
import com.huajie.domain.entity.Tenant;

import java.util.Objects;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
public class AssertUtil {

    public static void isTrue(boolean expression, String message){
        if (!expression){
            throw new ServerException(message);
        }
    }

    public static void nonNull(Object o, String message) {
        if (Objects.isNull(o)){
            throw new ServerException(message);
        }
    }
}
