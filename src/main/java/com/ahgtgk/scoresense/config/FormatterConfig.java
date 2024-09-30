package com.ahgtgk.scoresense.config;

import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.extension.spring.converter.StatusConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class FormatterConfig {

    @Bean
    public Converter<String, Status> swipeStatusConverter() {
        return new StatusConverter();
    }

}
