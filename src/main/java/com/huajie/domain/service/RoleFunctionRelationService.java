package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.RoleFunctionRelation;
import com.huajie.infrastructure.mapper.RoleFunctionRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class RoleFunctionRelationService {

    @Autowired
    private RoleFunctionRelationMapper roleFunctionRelationMapper;

    public List<RoleFunctionRelation> getFunctionRelationByRoleId(Integer roleId) {

        QueryWrapper<RoleFunctionRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleFunctionRelation::getRoleId, roleId);
        return roleFunctionRelationMapper.selectList(queryWrapper);
    }
}
