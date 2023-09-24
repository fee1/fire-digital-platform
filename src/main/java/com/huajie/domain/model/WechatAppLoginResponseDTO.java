package com.huajie.domain.model;

import lombok.Data;

/**
 * @author zhuxiaofeng
 * @date 2023/9/19
 */
@Data
public class WechatAppLoginResponseDTO {

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回
     */
    private String unionId;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 错误码
     *
     * 40029	code 无效	js_code无效
     * 45011	api minute-quota reach limit  mustslower  retry next minute	API 调用太频繁，请稍候再试
     * 40226	code blocked	高风险等级用户，小程序登录拦截 。风险等级详见用户安全解方案
     * -1	system error	系统繁忙，此时请开发者稍候再试
     */
    private String errcode;

}
