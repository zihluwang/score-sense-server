package com.ronglankj.scoresense.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class CacheConfig {

    @Bean
    public RedisSerializer<String> redisStringSerialiser() {
        return RedisSerializer.string();
    }

    @Bean
    public RedisTemplate<String, String> wechatAccessTokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        var redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(redisStringSerialiser());
        redisTemplate.setValueSerializer(redisStringSerialiser());
        return redisTemplate;
    }

}
