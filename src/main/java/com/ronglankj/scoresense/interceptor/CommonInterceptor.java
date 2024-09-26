package com.ronglankj.scoresense.interceptor;

import com.ronglankj.scoresense.context.RequestContext;
import com.ronglankj.scoresense.holder.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestContextHolder.setRequestContext(RequestContext.builder()
                .requestId(UUID.randomUUID().toString())
                .build());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContextHolder.removeRequestContext();
    }
}
