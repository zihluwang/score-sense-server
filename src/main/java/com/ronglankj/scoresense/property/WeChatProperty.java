package com.ronglankj.scoresense.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.wechat")
public class WeChatProperty {

    /**
     * Mini App id
     */
    private String appId;

    /**
     * Mini App secret
     */
    private String appSecret;

}
