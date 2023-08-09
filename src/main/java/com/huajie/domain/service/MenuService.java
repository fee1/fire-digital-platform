package com.huajie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huajie.domain.entity.Menu;
import com.huajie.infrastructure.mapper.MenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public Page<Menu> getPageMenuList(Integer pageNum, Integer pageSize, String menuCode, String menuName) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menuCode)){
            queryWrapper.lambda().eq(Menu::getMenuCode, menuCode);
        }
        if (StringUtils.isNotBlank(menuName)){
            queryWrapper.lambda().like(Menu::getMenuName, menuName);
        }
        return (Page<Menu>) menuMapper.selectList(queryWrapper);
    }
}
