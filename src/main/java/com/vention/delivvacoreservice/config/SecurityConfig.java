package com.vention.delivvacoreservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    private static final List<String> AUTHORIZED_DOMAINS = List.of(
            "delivva-dispute-env.eba-chhhwrqq.eu-north-1.elasticbeanstalk"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new IpRequestMatcher()).permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "USER")
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    static class IpRequestMatcher implements RequestMatcher {
        private final List<String> authorizedDomains;

        IpRequestMatcher() {
            this.authorizedDomains = AUTHORIZED_DOMAINS;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            String clientDomain = request.getHeader("source");
            if (Objects.nonNull(clientDomain)) {
                System.out.println("CLIENT_HOST: " + clientDomain);
                return authorizedDomains.stream()
                        .anyMatch(clientDomain::contains);
            }
            return false;
        }
    }
}
