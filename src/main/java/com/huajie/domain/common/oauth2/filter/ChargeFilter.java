package com.huajie.domain.common.oauth2.filter;

import com.huajie.domain.common.exception.PermissionException;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * todo 收费拦截 and 使用期限到期拦截
 *
 * @author zhuxiaofeng
 * @date 2023/9/13
 */
@Component
@Slf4j
public class ChargeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Tenant currentTenant = UserContext.getCurrentTenant();
            if (currentTenant.getStatus() == 0){
                throw new PermissionException("请及时缴费后，再尝试使用");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("未登录, 直接放行", e);
        }

        filterChain.doFilter(request, response);
    }
}
