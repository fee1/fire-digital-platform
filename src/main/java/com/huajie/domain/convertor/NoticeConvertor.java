package com.huajie.domain.convertor;

import com.alibaba.fastjson.JSONObject;
import com.huajie.domain.entity.Notice;
import com.huajie.domain.model.SysCreateNotice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;

/**
 * @author zhuxiaofeng
 * @date 2024/1/28
 */
@Mapper(componentModel = "spring",imports = {Arrays.class, JSONObject.class})
public interface NoticeConvertor {

    @Mapping(expression = "java(JSONObject.toJSONString(createNotice.getTenantIds()))", target = "tenantIds")
    @Mapping(expression = "java(JSONObject.toJSONString(createNotice.getAppendix()))", target = "appendix")
    Notice sysCreateNoticeToNotice(SysCreateNotice createNotice);

}
