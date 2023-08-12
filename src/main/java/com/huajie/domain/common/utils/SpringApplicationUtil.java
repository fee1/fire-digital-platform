package com.huajie.domain.common.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author zhuxiaofeng
 * @date 2023/6/20
 */
public class SpringApplicationUtil {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

}
