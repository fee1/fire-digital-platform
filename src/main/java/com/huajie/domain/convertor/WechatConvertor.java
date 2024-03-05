package com.huajie.domain.convertor;

import com.huajie.application.api.response.WechatAppLoginResponseVO;
import com.huajie.domain.common.oauth2.token.WechatOAuth2AccessToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WechatConvertor {

    @Mapping(source = "value", target = "accessToken")
    WechatAppLoginResponseVO wechatOAuth2AccessToken2ResponseDTO(WechatOAuth2AccessToken wechatOAuth2AccessToken);

}
