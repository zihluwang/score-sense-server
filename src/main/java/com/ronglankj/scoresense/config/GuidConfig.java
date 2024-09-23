package com.ronglankj.scoresense.config;

import com.onixbyte.guid.GuidCreator;
import com.onixbyte.guid.impl.SnowflakeGuidCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
public class GuidConfig {

    private final ZonedDateTime projectStartDateTime = LocalDateTime.of(2024, 9, 1, 0, 0, 0)
            .atZone(ZoneId.systemDefault());

    @Bean
    public GuidCreator<Long> userIdCreator() {
        return new SnowflakeGuidCreator(0x0, 0x0, projectStartDateTime.toEpochSecond());
    }

    @Bean
    public GuidCreator<Long> adminIdCreator() {
        return new SnowflakeGuidCreator(0x0, 0x1, projectStartDateTime.toEpochSecond());
    }

    @Bean
    public GuidCreator<Long> swipeIdCreator() {
        return new SnowflakeGuidCreator(0x1, 0x0, projectStartDateTime.toEpochSecond());
    }

    @Bean
    public GuidCreator<Long> attachmentIdCreator() {
        return new SnowflakeGuidCreator(0x1, 0x1, projectStartDateTime.toEpochSecond());
    }

    @Bean
    public GuidCreator<Long> examTypeIdCreator() {
        return new SnowflakeGuidCreator(0x1, 0x2, projectStartDateTime.toEpochSecond());
    }

}
