package com.huajie.common.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试使用
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@RestController
@RequestMapping("/user")
public class UserTestController {

    @GetMapping("/getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {
        return authentication.getPrincipal();
    }

}
