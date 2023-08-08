package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.application.api.common.exception.ApiException;
import com.huajie.domain.entity.SysDic;
import com.huajie.domain.entity.SysDicValue;
import com.huajie.domain.model.DicValueAddDTO;
import com.huajie.infrastructure.mapper.SysDicMapper;
import com.huajie.infrastructure.mapper.SysDicValueMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/8
 */
@Service
public class SysDicService {

    @Autowired
    private SysDicMapper sysDicMapper;

    @Autowired
    private SysDicValueMapper sysDicValueMapper;

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

    public void addDic(String dicCode, String dicName, String description) {
        QueryWrapper<SysDic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDic::getDicCode, dicCode);
        List<SysDic> sysDics = sysDicMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(sysDics)){
            throw new ApiException("字典code不能与先存的重复");
        }
        SysDic sysDic = new SysDic();
        sysDic.setDicName(dicName);
        sysDic.setDicCode(dicCode);
        sysDic.setDescription(description);
        sysDicMapper.insert(sysDic);
    }

    public List<SysDicValue> getDicValueList(String dicCode) {
        QueryWrapper<SysDicValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDicValue::getDicCode, dicCode);
        return sysDicValueMapper.selectList(queryWrapper);
    }

    public void addDicValue(DicValueAddDTO dicValueAddDTO) {
        SysDicValue sysDicValue = new SysDicValue();
        BeanUtils.copyProperties(dicValueAddDTO, sysDicValue);
        sysDicValueMapper.insert(sysDicValue);
    }
}
