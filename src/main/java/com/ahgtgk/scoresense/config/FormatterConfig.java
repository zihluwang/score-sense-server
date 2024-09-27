package com.ahgtgk.scoresense.config;

import com.ahgtgk.scoresense.enumeration.SwipeStatus;
import com.ahgtgk.scoresense.extension.spring.converter.SwipeStatusConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class FormatterConfig {

    @Bean
    public Converter<String, SwipeStatus> swipeStatusConverter() {
        return new SwipeStatusConverter();
    }

}
