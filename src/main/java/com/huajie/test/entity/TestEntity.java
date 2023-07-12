package com.huajie.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("TEST_TABLE")
public class TestEntity {

    private Long id;

    private String name;

}
