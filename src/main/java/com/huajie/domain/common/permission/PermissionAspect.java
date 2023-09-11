package com.huajie.domain.common.permission;

import com.huajie.domain.common.exception.PermissionException;
import com.huajie.domain.common.oauth2.model.CustomizeGrantedAuthority;
import com.huajie.domain.common.utils.SpringApplicationUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Function;
import com.huajie.domain.entity.Role;
import com.huajie.domain.entity.RoleFunctionRelation;
import com.huajie.domain.entity.Tenant;
import com.huajie.domain.service.FunctionService;
import com.huajie.domain.service.RoleFunctionRelationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Pointcut("@annotation(com.huajie.domain.common.permission.Permission)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Permission annotation = null;
        try {
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            annotation = AnnotationUtils.findAnnotation(method, Permission.class);
        }catch (Exception e){
            log.error("获取注解失败, ", e);
            e.printStackTrace();
            throw new PermissionException("获取注解失败");
        }
        String functionCode = annotation.functionCode();
        FunctionService functionService = SpringApplicationUtil.getApplicationContext().getBean(FunctionService.class);
        Function function = functionService.getFunctionByFuntionCode(functionCode);
        //对登录态判断
        if (function.getLimitLogin()){
            if (!UserContext.isLogin()){
                throw new PermissionException("请登录后访问");
            }
        }
        CustomizeGrantedAuthority customizeGrantedAuthority = UserContext.getCustomizeGrantedAuthority();
        //对权限判断
        if (function.getLimitAuth()){

            List<RoleFunctionRelation> roleFunctionRelations = customizeGrantedAuthority.getRoleFunctionRelations();
            if (CollectionUtils.isEmpty(roleFunctionRelations)){
                throw new PermissionException("无权限访问此方法");
            }
            Integer currentId = function.getId();
            Set<Integer> roleFunctionRelationIds = roleFunctionRelations.stream().map(RoleFunctionRelation::getId).collect(Collectors.toSet());
            if (!roleFunctionRelationIds.contains(currentId)){
                throw new PermissionException("无权限访问此方法");
            }
        }
        //对收费判断
        if (function.getLimitFee()){
            Tenant tenant = customizeGrantedAuthority.getTenant();
            if (tenant.getStatus() == 0){
                throw new PermissionException("请及时缴费后，再尝试使用");
            }
        }
        return joinPoint.proceed();
    }

}
