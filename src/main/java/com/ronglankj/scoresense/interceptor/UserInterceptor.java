package com.ronglankj.scoresense.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.simplejwt.TokenResolver;
import com.ronglankj.scoresense.context.UserContext;
import com.ronglankj.scoresense.holder.UserContextHolder;
import com.ronglankj.scoresense.model.payload.UserPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用于拦截用户登录状态及存储用户信息的拦截器。
 *
 * @author zihluwan
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private final TokenResolver<DecodedJWT> tokenResolver;

    public UserInterceptor(TokenResolver<DecodedJWT> tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token
        var token = request.getHeader("Authorization");

        try {
            // 将 token 解析为 User
            var userPayload = tokenResolver.extract(token, UserPayload.class);

            // 将 User 存放至 UserContext 中
            var userContext = UserContext.builder()
                    .id(userPayload.getId())
                    .openId(userPayload.getOpenId())
                    .build();

            // 将 UserContext 存放至 UserContextHolder 中
            UserContextHolder.setUserContext(userContext);
        } catch (JWTVerificationException verificationException) {
            log.error("无法解析用户身份令牌。", verificationException);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成，清理 UserContextHolder
        UserContextHolder.removeUserContext();
    }
}
