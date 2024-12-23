package com.ronglankj.scoresense.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.simplejwt.TokenResolver;
import com.ronglankj.scoresense.context.AdminContext;
import com.ronglankj.scoresense.context.UserContext;
import com.ronglankj.scoresense.holder.AdminContextHolder;
import com.ronglankj.scoresense.holder.UserContextHolder;
import com.ronglankj.scoresense.model.payload.AdminPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用于获取请求中的管理员信息的拦截器。
 *
 * @author zihluwang
 */
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final TokenResolver<DecodedJWT> tokenResolver;

    public AdminInterceptor(TokenResolver<DecodedJWT> tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token
        var token = request.getHeader("Authorization");

        try {
            // 将 token 解析为 Admin
            var admin = tokenResolver.extract(token, AdminPayload.class);

            // 将 Admin 存放至 AdminContext 中
            var adminCtx = AdminContext.builder()
                    .admin(admin.toPersistent())
                    .build();

            // 将 AdminContext 存放至 AdminContextHolder 中
            AdminContextHolder.setAdminContext(adminCtx);
        } catch (JWTVerificationException verificationException) {
            log.error("无法解析管理员身份令牌。", verificationException);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成，清理 AdminContextHolder
        AdminContextHolder.removeAdminContext();
    }

}
