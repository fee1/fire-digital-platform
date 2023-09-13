package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.entity.Function;
import com.huajie.infrastructure.mapper.FunctionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Service
public class FunctionService {

    @Autowired
    private FunctionMapper functionMapper;

    public Page<Function> getPageFunctionList(Integer pageNum, Integer pageSize, String functionCode, String functionName) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Function> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(functionCode)){
            queryWrapper.lambda().eq(Function::getFunctionCode, functionCode);
        }
        if (StringUtils.isNotBlank(functionName)){
            queryWrapper.lambda().like(Function::getFunctionName, functionName);
        }
        return (Page<Function>) functionMapper.selectList(queryWrapper);
    }


    public Function getFunctionByFuntionCode(String functionCode) {
        QueryWrapper<Function> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Function::getFunctionCode, functionCode);
        return functionMapper.selectOne(queryWrapper);
    }
}
