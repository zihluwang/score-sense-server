package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.security.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }

}
