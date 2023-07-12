package com.huajie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.huajie")
public class BootStarterTest {

    public static void main(String[] args) {
        try {
            SpringApplication.run(BootStarterTest.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
