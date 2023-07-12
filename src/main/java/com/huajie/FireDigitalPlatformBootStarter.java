package com.huajie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhuxiaofeng
 * @date 2023/7/10
 */
@SpringBootApplication
@MapperScan("com.huajie.**.mapper")
public class FireDigitalPlatformBootStarter {

    public static void main(String[] args) {
        try {
            SpringApplication.run(FireDigitalPlatformBootStarter.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
