//package com.huajie.domain.common.oauth2.filter;
//
//import com.huajie.domain.common.exception.PermissionException;
//import com.huajie.domain.common.utils.DateUtil;
//import com.huajie.domain.common.utils.UserContext;
//import com.huajie.domain.entity.Tenant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//
///**
// * todo 收费拦截 and 使用期限到期拦截
// *
// * @author zhuxiaofeng
// * @date 2023/9/13
// */
////@Component
//@Slf4j
//public class ChargeFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            Tenant currentTenant = UserContext.getCurrentTenant();
//            if (currentTenant.getStatus() == 0){
//                throw new PermissionException("请及时缴费后，再尝试使用");
//            }
//            //拦截到期用户，并生成订单号，生成过就不再生成
//            long result = DateUtil.dateSub(new Date(), currentTenant.getEffectiveEndDate());
//            log.info("还剩 {} 天", result);
//            if (result > 0){
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                return;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("未登录, 直接放行", e);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
