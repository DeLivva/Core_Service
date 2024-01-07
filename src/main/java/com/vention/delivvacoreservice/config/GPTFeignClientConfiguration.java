package com.vention.delivvacoreservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

// If @Configuration annotations is written here, it will apply for all fiegn clients.
public class GPTFeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CustomRequestInterceptor();
    }

    private static class CustomRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header("Authorization", "Bearer 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");
        }
    }
}
