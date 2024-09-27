package com.ahgtgk.scoresense.interceptor;

import com.ahgtgk.scoresense.context.RequestContext;
import com.ahgtgk.scoresense.holder.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        RequestContextHolder.setRequestContext(RequestContext.builder()
                .requestId(UUID.randomUUID().toString())
                .build());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        RequestContextHolder.removeRequestContext();
    }
}
