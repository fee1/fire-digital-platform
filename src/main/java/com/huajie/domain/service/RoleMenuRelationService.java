package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajie.domain.entity.RoleMenuRelation;
import com.huajie.infrastructure.mapper.RoleMenuRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Service
public class RoleMenuRelationService {

    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;

    public List<RoleMenuRelation> getMenuRelationByRoleId(Integer roleId) {
        QueryWrapper<RoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuRelation::getRoleId, roleId);
        return roleMenuRelationMapper.selectList(queryWrapper);
    }
}
