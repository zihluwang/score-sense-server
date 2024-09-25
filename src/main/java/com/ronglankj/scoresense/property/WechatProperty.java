package com.ronglankj.scoresense.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.wechat")
public class WechatProperty {

    /**
     * 小程序 ID
     */
    private String appId;

    /**
     * 小程序密钥
     */
    private String appSecret;

}
