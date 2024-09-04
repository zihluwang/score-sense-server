package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.property.WeChatProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WeChatProperty.class})
public class WeChatConfig {
}
