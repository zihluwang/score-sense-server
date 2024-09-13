package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.property.WeChatProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序相关配置
 *
 * @author zihluwang
 */
@Configuration
@EnableConfigurationProperties({WeChatProperty.class})
public class WeChatConfig {

    /**
     * 微信服务端 Host
     */
    public static final String ENTRYPOINT_HOST = "";

}
