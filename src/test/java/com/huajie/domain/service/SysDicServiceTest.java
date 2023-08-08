package com.huajie.domain.service;

import com.huajie.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
public class SysDicServiceTest extends BaseTest {

    @Autowired
    private SysDicService sysDicService;

    @Test
    public void addDic(){
        sysDicService.addDic("test", "test", "test");
    }

}