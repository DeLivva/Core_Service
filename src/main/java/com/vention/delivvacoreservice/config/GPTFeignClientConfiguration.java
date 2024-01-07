package com.vention.delivvacoreservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

// If @Configuration annotations is written here, it will apply for all fiegn clients.
@RequiredArgsConstructor
public class GPTFeignClientConfiguration {

    private final OpenApiConfig openApiConfig;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CustomRequestInterceptor();
    }

    private class CustomRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header("Authorization", "Bearer " + openApiConfig.getOpenApiSecretKey());
        }
    }
}
