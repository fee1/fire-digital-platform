package com.huajie.domain.common.mybatis.config;

import com.huajie.domain.common.constants.CommonConstants;
import com.huajie.domain.common.utils.ObjectReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.util.Date;
import java.util.Properties;

/**
 * @author zhuxiaofeng
 * @date 2023/10/22
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class ParamInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            for (int i = 1; i < args.length; i++) {
                try {
                    Object param = args[i];
                    if (SqlCommandType.INSERT == ms.getSqlCommandType()) {
                        Object createUser = ObjectReflectUtil.getFieldValue(param, CommonConstants.CREATE_USER);
                        if (createUser == null){
                            ObjectReflectUtil.setFieldValue(param, CommonConstants.CREATE_USER, "");
                        }
                        Object createTime = ObjectReflectUtil.getFieldValue(param, CommonConstants.CREATE_TIME);
                        if (createTime == null){
                            ObjectReflectUtil.setFieldValue(param, CommonConstants.CREATE_TIME, new Date());
                        }
                    }
                    if (SqlCommandType.UPDATE == ms.getSqlCommandType()){
                        Object updateUser = ObjectReflectUtil.getFieldValue(param, CommonConstants.UPDATE_USER);
                        if (updateUser == null){
                            ObjectReflectUtil.setFieldValue(param, CommonConstants.UPDATE_USER, "");
                        }
                        Object updateTime = ObjectReflectUtil.getFieldValue(param, CommonConstants.UPDATE_TIME);
                        if (updateTime == null){
                            ObjectReflectUtil.setFieldValue(param, CommonConstants.UPDATE_TIME, new Date());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("ParamInterceptor exception", e);
                }
            }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
