package com.huajie.application.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.huajie.application.api.common.ApiResult;
import com.huajie.application.api.common.ResultCode;
import com.huajie.domain.common.utils.DateUtil;
import com.huajie.domain.common.utils.UserContext;
import com.huajie.domain.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 收费拦截 and 使用期限到期拦截
 */
@Slf4j
public class ChargeInterceptor implements HandlerInterceptor {

    private final String contentType = "application/json;charset=UTF-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Tenant currentTenant = UserContext.getCurrentTenant();
        if (currentTenant.getStatus() == 0){
            log.error("该用户已被禁用, tenant = {}", JSONObject.toJSONString(currentTenant));
            ApiResult<Void> responseBody = ApiResult.failed(ResultCode.REQUEST_NOT_PERMISSION, "用户已被禁用");
            response.setContentType(contentType);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(JSONObject.toJSONString(responseBody));
            return false;
        }
        Date effectiveEndDate = currentTenant.getEffectiveEndDate();
        long lastDaysNum = DateUtil.dateSub(effectiveEndDate, new Date());
        if (lastDaysNum > 0){
            log.info("tanantName = {}, tenantId = {}, 距离缴费日期, 剩余 {} 天", currentTenant.getTenantName(),
                    currentTenant.getId(), lastDaysNum);
        }else {
            log.error("该租户使用期限到期, tenant = {}", JSONObject.toJSONString(currentTenant));
            ApiResult<Void> responseBody = ApiResult.failed(ResultCode.TENANT_MEMBER_EXPIRE);
            response.setContentType(contentType);
            response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
            response.getWriter().print(JSONObject.toJSONString(responseBody));
            return false;
        }
        return true;
    }

}
