package com.huajie;

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
            SpringApplication.run(FireDigitalPlatformBootStarter.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
