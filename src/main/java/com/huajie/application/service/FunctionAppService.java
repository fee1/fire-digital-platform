package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.response.FunctionResponseVO;
import com.huajie.domain.entity.Function;
import com.huajie.domain.service.FunctionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Service
public class FunctionAppService {

    @Autowired
    private FunctionService functionService;

    public Page<FunctionResponseVO> getPageFunctionList(Integer pageNum, Integer pageSize, String functionCode, String functionName) {
        Page<Function> page = functionService.getPageFunctionList(pageNum, pageSize, functionCode, functionName);
        Page<FunctionResponseVO> functionResponseVOS = new Page<>();
        BeanUtils.copyProperties(page, functionResponseVOS);
        for (Function function : page) {
            FunctionResponseVO functionResponseVO = new FunctionResponseVO();
            BeanUtils.copyProperties(function, functionResponseVO);
            functionResponseVOS.add(functionResponseVO);
        }
        return functionResponseVOS;
    }
}
