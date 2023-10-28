package com.huajie.application.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuxiaofeng
 * @date 2023/10/28
 */
@Data
public class PublicNoticeRequestVO {

    @NotNull(message = "公告id 不能为空")
    private Integer id;

}
