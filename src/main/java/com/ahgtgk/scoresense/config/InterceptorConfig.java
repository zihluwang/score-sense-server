package com.ahgtgk.scoresense.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.simplejwt.TokenResolver;
import com.ahgtgk.scoresense.interceptor.AdminInterceptor;
import com.ahgtgk.scoresense.interceptor.CommonInterceptor;
import com.ahgtgk.scoresense.interceptor.UserInterceptor;
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

    @Bean
    public CommonInterceptor commonInterceptor() {
        return new CommonInterceptor();
    }

}
