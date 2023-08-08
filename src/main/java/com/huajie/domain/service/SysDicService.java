package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.SysDic;
import com.huajie.infrastructure.mapper.SysDicMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Service
public class SysDicService {

    @Autowired
    private SysDicMapper sysDicMapper;

    public List<SysDic> getDicList(String dicCode, String description) {
        QueryWrapper<SysDic> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dicCode)) {
            queryWrapper.lambda().eq(SysDic::getDicCode, dicCode);
        }
        if (StringUtils.isNotBlank(description)){
            queryWrapper.lambda().like(SysDic::getDescription, description);
        }
        return sysDicMapper.selectList(queryWrapper);
    }
}
