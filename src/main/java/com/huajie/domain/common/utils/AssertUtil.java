package com.huajie.domain.common.utils;

import com.huajie.domain.common.exception.ServerException;

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

    public static void isTrue(boolean expression, RuntimeException e){
        if (!expression){
            throw e;
        }
    }

    public static void nonNull(Object o, String message) {
        if (Objects.isNull(o)){
            throw new ServerException(message);
        }
    }

    public static void nonNull(Object o, RuntimeException e) {
        if (Objects.isNull(o)){
            throw e;
        }
    }
}
