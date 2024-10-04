package com.ahgtgk.scoresense.config;

import com.ahgtgk.scoresense.enumeration.Status;
import com.ahgtgk.scoresense.interceptor.CommonInterceptor;
import com.ahgtgk.scoresense.property.CorsProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties({CorsProperty.class})
public class WebConfig implements WebMvcConfigurer {

    private CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**");
    }

    @Autowired
    public void setCommonInterceptor(CommonInterceptor commonInterceptor) {
        this.commonInterceptor = commonInterceptor;
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperty corsProperty) {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.stream(corsProperty.getAllowedOrigins()).toList());
        configuration.setAllowedMethods(Arrays.stream(corsProperty.getAllowedMethods()).map(RequestMethod::name).toList());
        configuration.setAllowedHeaders(Arrays.stream(corsProperty.getAllowedHeaders()).toList());
        configuration.setExposedHeaders(Arrays.stream(corsProperty.getExposedHeaders()).toList());

        Optional.of(corsProperty)
                .map(CorsProperty::getAllowCredentials)
                .ifPresent(configuration::setAllowCredentials);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
