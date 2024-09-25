package com.ronglankj.scoresense.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveWebConfig {

    @Bean
    public WebClient wechatClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.weixin.qq.com")
                .build();
    }

}
