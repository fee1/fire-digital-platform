package com.huajie.application.api.response;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/7
 */
@Data
public class UserDetailResponseVO {

    private Integer id;

    private String userNo;

    private String userName;

    private String phone;

    private String email;

    private String roleName;

}
