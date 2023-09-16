package com.huajie.domain.common.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 *
 * @author zhuxiaofeng
 * @date 2023/7/11
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                //登录认证拦截的路径
                .antMatchers("/oauth/**",
                        "/login/**",
                        "/logout/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                //禁止跳转到默认登录页面
                .disable();
//                .failureHandler(customAuthenticationFailureHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //放行的url
        web.ignoring().antMatchers(
                "/register/**", "/alipay/notify/**", "/region/**",
                "/sys/dic/list", "/sys/dic/value/list",
                "/pay/callback",
                "/pay/record/select",
                "/pay/generate/qrcode/image",
                //放行 swagger
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html","/css/**", "/js/**", "/webjars/**"
        );
    }
}
