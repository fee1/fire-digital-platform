package com.huajie.domain.common.utils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/11/29
 */
public class GuavaUtilTest {

    public static void main(String[] args) throws InterruptedException {
        GuavaUtil.set("test", "t1", 10);
        System.out.println((String) GuavaUtil.get("test"));
        Thread.sleep(10000);
        System.out.println((String) GuavaUtil.get("test"));

        GuavaUtil.set("test", "t1", 100);
        System.out.println((String) GuavaUtil.get("test"));
        Thread.sleep(1000);
        System.out.println((String) GuavaUtil.get("test"));

        GuavaUtil.set("test", "t2", 1000);
        System.out.println((String) GuavaUtil.get("test"));

        GuavaUtil.remove("test");
        Thread.sleep(1000);
        System.out.println((String) GuavaUtil.get("test"));
    }

}