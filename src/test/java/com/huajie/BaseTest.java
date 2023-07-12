package com.huajie;

import com.alibaba.fastjson.JSON;
import com.huajie.test.entity.TestEntity;
import com.huajie.test.mapper.TestMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 逻辑测试基类
 *
 * @author zxf
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FireDigitalPlatformBootStarter.class)
public class BaseTest {

    @Autowired
    private TestMapper testMapper;

    @Test
    public void testSelect() {
        TestEntity testEntity = testMapper.selectById(1);
        System.out.printf(JSON.toJSONString(testEntity));
    }

}
