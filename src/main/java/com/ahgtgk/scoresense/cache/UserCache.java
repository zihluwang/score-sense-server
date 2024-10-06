package com.ahgtgk.scoresense.cache;

import com.ahgtgk.scoresense.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCache {

    private final RedisTemplate<String, User> userRedisTemplate;

    public UserCache(@Qualifier("userRedisTemplate") RedisTemplate<String, User> userRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
    }

    public void putUser(User user) {
        userRedisTemplate.opsForValue().set("totalScore-sense:user:" + user.getId(), user);
    }

    public User getUser(Long id) {
        return userRedisTemplate.opsForValue().get("totalScore-sense:user:" + id);
    }

}
