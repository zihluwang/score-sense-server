package com.ronglankj.scoresense.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperty {

    private RequestMethod[] allowedMethods = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PATCH,
            RequestMethod.PUT,
            RequestMethod.DELETE,
    };

    private Boolean allowCredentials = true;

    private String[] allowedOrigins = {"https://exam1.ronglankj.com"};

    private String[] allowedHeaders = {"Content-Type", "Authorization"};

    private String[] exposedHeaders = {"Content-Type", "Authorization"};

}