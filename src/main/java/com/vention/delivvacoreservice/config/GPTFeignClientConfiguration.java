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
            char[] secretKey = {'s', 'k', '-', '8', 'F', 'o', 'X', 'q', 'f', '9', 'W', '0', 'F', 'C', '8', 'k', 'h', 'k', 'g', 'c', 'Q', 'O', 'O', 'T', '3', 'B', 'l', 'b', 'k', 'F', 'J', 'O', 'C', 'K', 'w', 'h', 'Z', '3', 'g', 'e', 'r', 'Y', 'U', 'o', 'X', '9', 'P', '0', 'C', 'm', 'B'};

            template.header("Authorization", "Bearer " + new String(secretKey));
        }
    }
}
