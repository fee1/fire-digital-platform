package com.huajie.domain.common.oauth2.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/8/6
 */
@Data
public class RoleModel {

    private Integer id;

    private String roleCode;

    private String roleName;

    private String description;
}
