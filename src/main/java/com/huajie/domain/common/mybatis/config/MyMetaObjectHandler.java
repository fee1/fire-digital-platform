package com.huajie.domain.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
        Object createUser = this.getFieldValByName("createUser", metaObject);
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if (Objects.isNull(createUser)){
            this.setFieldValByName("createUser", this.getCurrentUser().getUsername(), metaObject);
        }
        if (Objects.isNull(createTime)){
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateUser = this.getFieldValByName("updateUser", metaObject);
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(updateUser)){
            this.setFieldValByName("updateUser", this.getCurrentUser().getUsername(), metaObject);
        }
        if (Objects.isNull(updateTime)){
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

}
