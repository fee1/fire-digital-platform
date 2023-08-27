package com.huajie.domain.common.exception;

/**
 * 服务内部异常
 *
 * @author zhuxiaofeng
 * @date 2023/8/21
 */
public class ServerException extends RuntimeException {

    public ServerException(String message){
        super(message);
    }

}
