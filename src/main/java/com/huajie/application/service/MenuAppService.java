package com.huajie.application.service;

import com.github.pagehelper.Page;
import com.huajie.application.api.response.MenuResponseVO;
import com.huajie.domain.entity.Menu;
import com.huajie.domain.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxiaofeng
 * @date 2023/8/9
 */
@Service
public class MenuAppService {

    @Autowired
    private MenuService menuService;

    public Page<MenuResponseVO> getPageMenuList(Integer pageNum, Integer pageSize, String menuCode, String menuName) {
        Page<Menu> page = menuService.getPageMenuList(pageNum, pageSize, menuCode, menuName);
        Page<MenuResponseVO> responseVOS = new Page<>();
        BeanUtils.copyProperties(page, responseVOS);
        for (Menu menu : page) {
            MenuResponseVO menuResponseVO = new MenuResponseVO();
            BeanUtils.copyProperties(menu, menuResponseVO);
            responseVOS.add(menuResponseVO);
        }
        return responseVOS;
    }
}
