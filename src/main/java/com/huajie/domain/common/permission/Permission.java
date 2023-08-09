package com.huajie.domain.common.permission;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    @AliasFor("functionCode")
    String value() default "";

    @AliasFor("value")
    String functionCode() default "";

}
