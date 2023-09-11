package com.huajie.domain.common.exception;

/**
 * 认证异常
 *
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
public class PermissionException extends RuntimeException {

    public PermissionException(String message){
        super(message);
    }

}
