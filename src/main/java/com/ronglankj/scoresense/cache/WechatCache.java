package com.ronglankj.scoresense.cache;

import com.ronglankj.scoresense.model.biz.WechatAccessTokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class WechatCache {

    private final RedisTemplate<String, String> wechatAccessTokenRedisTemplate;

    public WechatCache(@Qualifier("wechatAccessTokenRedisTemplate") RedisTemplate<String, String> wechatAccessTokenRedisTemplate) {
        this.wechatAccessTokenRedisTemplate = wechatAccessTokenRedisTemplate;
    }

    public void saveAccessToken(WechatAccessTokenResponse wechatAccessTokenResponse) {
        wechatAccessTokenRedisTemplate.opsForValue()
                .set("score-sense:wechat:access-token",
                        wechatAccessTokenResponse.accessToken(),
                        Duration.ofSeconds(wechatAccessTokenResponse.expiresIn()));
    }

    public String getAccessToken() {
        return wechatAccessTokenRedisTemplate.opsForValue().get("score-sense:wechat:access-token");
    }

}
