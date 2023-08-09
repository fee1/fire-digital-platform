package com.huajie.domain.common.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Aspect
@Component
public class PermissionAspect {

    @Pointcut("@annotation(com.huajie.domain.common.permission.Permission)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint pjp){
        return null;
    }

}
