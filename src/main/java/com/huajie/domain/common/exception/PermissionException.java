package com.huajie.domain.common.exception;

import com.huajie.application.api.common.IErrorCode;
import lombok.Getter;

/**
 * 认证异常
 *
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
public class PermissionException extends RuntimeException {

    @Getter
    private IErrorCode errorCode;

    public PermissionException(String message, IErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
