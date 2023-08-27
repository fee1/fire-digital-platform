package com.huajie.application.api.common.exception;


import com.huajie.application.api.common.ApiResult;
import com.huajie.domain.common.exception.ServerException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕捉
 *
 * @author zhuxiaofeng
 * @date 2021/9/4
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 接口异常捕捉
     * @param ex 异常
     * @return api
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public ApiResult<Void> handleException(Exception ex){
        return ApiResult.failed(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<Void> handleValidException(MethodArgumentNotValidException validException){
        BindingResult bindingResult = validException.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            message = fieldError.getField() + fieldError.getDefaultMessage();
        }
        return ApiResult.validFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ApiResult<Void> handleValidException(BindException bindException){
        BindingResult bindingResult = bindException.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            message = fieldError.getField() + fieldError.getDefaultMessage();
        }
        return ApiResult.validFailed(message);
    }

    /**
     * oauth2 异常报错
     * @param oAuth2Exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public ApiResult<Void> handleOAuth2Exception(OAuth2Exception oAuth2Exception){
        return ApiResult.failed(oAuth2Exception.getMessage());
    }

    /**
     * 服务异常处理
     * @param serverException
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServerException.class)
    public ApiResult<Void> handleServerException(ServerException serverException){
        return ApiResult.failed("服务异常：" + serverException.getMessage());
    }


}
