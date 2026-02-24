package com.invex.testinvex.employee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(auth -> auth
                        // Swagger / OpenAPI
                        .antMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()

                        // Actuator
                        .antMatchers("/actuator/health", "/actuator/info").permitAll()

                        // H2 console
                        .antMatchers("/h2-console/**").permitAll()

                        // All security
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        // Enable H2 console in frames
        http.headers().frameOptions().disable();

        return http.build();
    }
}
