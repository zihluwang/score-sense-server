package com.ronglankj.scoresense.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.simplejwt.TokenResolver;
import com.ronglankj.scoresense.entity.Admin;
import com.ronglankj.scoresense.interceptor.AdminInterceptor;
import com.ronglankj.scoresense.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 拦截器配置。
 *
 * @author zihluwang
 */
@Configuration
public class InterceptorConfig {

    @Bean
    public UserInterceptor userInterceptor(TokenResolver<DecodedJWT> tokenResolver) {
        return new UserInterceptor(tokenResolver);
    }

    @Bean
    public AdminInterceptor adminInterceptor(TokenResolver<DecodedJWT> tokenResolver) {
        return new AdminInterceptor(tokenResolver);
    }


}
