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

    public void saveAccessToken(String accessToken) {
        wechatAccessTokenRedisTemplate.opsForValue()
                .set("score-sense:wechat:access-token", accessToken, Duration.ofSeconds(7200));
    }

    public String getAccessToken() {
        return wechatAccessTokenRedisTemplate.opsForValue().get("score-sense:wechat:access-token");
    }

}
