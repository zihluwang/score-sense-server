package com.ahgtgk.scoresense.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class WechatCache {

    private final RedisTemplate<String, String> redisTemplate;

    public WechatCache(@Qualifier("wechatRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAccessToken(String accessToken) {
        redisTemplate.opsForValue()
                .set("totalScore-sense:wechat:access-token", accessToken, Duration.ofSeconds(7200));
    }

    public String getAccessToken() {
        return redisTemplate.opsForValue().get("totalScore-sense:wechat:access-token");
    }

}
