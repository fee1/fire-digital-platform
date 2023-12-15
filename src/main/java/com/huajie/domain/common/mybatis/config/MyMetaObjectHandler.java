package com.huajie.domain.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.huajie.domain.common.constants.CommonConstants;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.Objects;

/**
 * @author zhuxiaofeng
 * @date 2023/8/5
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createUser = this.getFieldValByName(CommonConstants.CREATE_USER, metaObject);
        Object createTime = this.getFieldValByName(CommonConstants.CREATE_TIME, metaObject);
        if (Objects.isNull(createUser)){
            this.setFieldValByName(CommonConstants.CREATE_USER, this.getCurrentUserName(), metaObject);
        }
        if (Objects.isNull(createTime)){
            this.setFieldValByName(CommonConstants.CREATE_TIME, new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateUser = this.getFieldValByName(CommonConstants.UPDATE_USER, metaObject);
        Object updateTime = this.getFieldValByName(CommonConstants.UPDATE_TIME, metaObject);
        if (Objects.isNull(updateUser)){
            this.setFieldValByName(CommonConstants.UPDATE_USER, this.getCurrentUserName(), metaObject);
        }
        if (Objects.isNull(updateTime)){
            this.setFieldValByName(CommonConstants.UPDATE_TIME, new Date(), metaObject);
        }
    }

    private String getCurrentUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            User principal = (User) auth.getPrincipal();
            if (principal != null){
                return principal.getUsername();
            }
            return "SYSTEM";
        }catch (Exception e){
            return "SYSTEM";
        }
    }

}
