package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.property.WechatProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序相关配置
 *
 * @author zihluwang
 */
@Configuration
@EnableConfigurationProperties({WechatProperty.class})
public class WechatConfig {

}
