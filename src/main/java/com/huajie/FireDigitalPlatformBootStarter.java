package com.huajie;

import com.huajie.domain.common.utils.SpringApplicationUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhuxiaofeng
 * @date 2023/7/10
 */
@SpringBootApplication
public class FireDigitalPlatformBootStarter {

    public static void main(String[] args) {
        try {
            SpringApplicationUtil.setApplicationContext(SpringApplication.run(FireDigitalPlatformBootStarter.class, args));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
