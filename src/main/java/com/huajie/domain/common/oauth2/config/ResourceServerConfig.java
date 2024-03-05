package com.huajie.domain.common.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器配置
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//    @Autowired
//    private ChargeFilter chargeFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .requestMatchers()
                //配置需要保护的资源路径
                .antMatchers("/user/**",
                                        "/role/**",
                        "/menu/**",
                        "/function/**",
                        "/place/**",
                        "/device/**",
                        "/goverment/organization/**",
                        "/sys/dic/add",
                        "/sys/dic/value/add",
                        "/pay/record/select",
                        "/inspect/**",
                        "/problemReform/**",
                        "/file/upload/**",
                        "/notice/**",
                        "/tenant/**",
                        "/monitor/**",
                        "/index/statistic/**",
                        "/wechat/edit/user/info",
                        "/wechat/user/management",
                        "/wechat/app/usr/logout",
                        "/weather/**",
                        "/pay/generate/pay/record"
                );
    }


}
