package com.ronglankj.scoresense.config;

import com.ronglankj.scoresense.interceptor.AdminInterceptor;
import com.ronglankj.scoresense.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserInterceptor userInterceptor;

    private AdminInterceptor adminInterceptor;

    @Autowired
    public void setUserInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Autowired
    public void setAdminInterceptor(AdminInterceptor adminInterceptor) {
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/login");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admins/**")
                .excludePathPatterns("/admins/login");
    }

}
