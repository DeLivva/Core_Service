package com.vention.delivvacoreservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.secret-key}")
    private String openApiSecretKey;

    public String getOpenApiSecretKey() {
        return openApiSecretKey;
    }
}
