package com.ahgtgk.scoresense.config;

import com.ahgtgk.scoresense.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class CacheConfig {

    @Bean
    public RedisSerializer<String> stringRedisSerializer() {
        return RedisSerializer.string();
    }

    /**
     * 微信 RedisTemplate。
     */
    @Bean
    public RedisTemplate<String, String> wechatRedisTemplate(RedisConnectionFactory connectionFactory) {
        var redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(stringRedisSerializer());
        return redisTemplate;
    }

    /**
     * 用户 Redis 序列化器
     */
    @Bean
    public RedisSerializer<User> userRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(User.class);
    }

    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        var redisTemplate = new RedisTemplate<String, User>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(userRedisSerializer());
        return redisTemplate;
    }

}
