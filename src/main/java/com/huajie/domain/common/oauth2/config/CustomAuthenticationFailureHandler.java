//package com.huajie.domain.common.oauth2.config;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        // 在认证失败时执行自定义逻辑
//        // 例如，返回自定义的错误信息
//        response.getWriter().write("Authentication failed: " + exception.getMessage());
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//}
