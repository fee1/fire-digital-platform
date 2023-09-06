package com.huajie.domain.common.exception;

/**
 * 应用启动异常
 *
 * @author zhuxiaofeng
 * @date 2023/8/21
 */
public class AppStarterException extends RuntimeException {

    public AppStarterException(String message){
        super(message);
    }

}
