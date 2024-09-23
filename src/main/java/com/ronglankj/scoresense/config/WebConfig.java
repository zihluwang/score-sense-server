package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.enumeration.SwipeStatus;
import com.ronglankj.scoresense.extension.spring.converter.SwipeStatusConverter;
import com.ronglankj.scoresense.interceptor.AdminInterceptor;
import com.ronglankj.scoresense.interceptor.CommonInterceptor;
import com.ronglankj.scoresense.interceptor.UserInterceptor;
import com.ronglankj.scoresense.property.CorsProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableConfigurationProperties({CorsProperty.class})
public class WebConfig implements WebMvcConfigurer {

    private UserInterceptor userInterceptor;
    private AdminInterceptor adminInterceptor;
    private CorsProperty corsProperty;
    private CommonInterceptor commonInterceptor;
    private Converter<String, SwipeStatus> swipeStatusConverter;

    @Autowired
    public void setUserInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Autowired
    public void setAdminInterceptor(AdminInterceptor adminInterceptor) {
        this.adminInterceptor = adminInterceptor;
    }

    @Autowired
    public void setCorsProperty(CorsProperty corsProperty) {
        this.corsProperty = corsProperty;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/login");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admins/**")
                .excludePathPatterns("/admins/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperty.getAllowedOrigins())
                .allowCredentials(corsProperty.getAllowCredentials())
                .allowedMethods(Arrays.stream(corsProperty.getAllowedMethods())
                        .map(RequestMethod::name)
                        .toArray(String[]::new))
                .allowedHeaders(corsProperty.getAllowedHeaders())
                .exposedHeaders(corsProperty.getExposedHeaders());
    }

    @Autowired
    public void setCommonInterceptor(CommonInterceptor commonInterceptor) {
        this.commonInterceptor = commonInterceptor;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(swipeStatusConverter);
    }

    @Autowired
    public void setSwipeStatusConverter(Converter<String, SwipeStatus> swipeStatusConverter) {
        this.swipeStatusConverter = swipeStatusConverter;
    }
}
