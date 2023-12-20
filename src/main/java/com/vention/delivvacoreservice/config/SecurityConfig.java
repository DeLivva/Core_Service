package com.vention.delivvacoreservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    private static final List<String> AUTHORIZED_DOMAINS = List.of(
            "http://delivva-dispute-env.eba-chhhwrqq.eu-north-1.elasticbeanstalk.com"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new IpRequestMatcher()).permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "USER")
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    static class IpRequestMatcher implements RequestMatcher {
        private final List<String> authorizedDomains;

        IpRequestMatcher() {
            this.authorizedDomains = AUTHORIZED_DOMAINS;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            String clientDomain = request.getServerName();
            return authorizedDomains.stream()
                    .anyMatch(clientDomain::contains);
        }
    }
}
