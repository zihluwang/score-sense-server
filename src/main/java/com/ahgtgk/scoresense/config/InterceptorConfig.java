package com.ahgtgk.scoresense.config;

import com.ahgtgk.scoresense.interceptor.CommonInterceptor;
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
    public CommonInterceptor commonInterceptor() {
        return new CommonInterceptor();
    }

}
