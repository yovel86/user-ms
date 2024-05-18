package com.projects.userservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests(
                        requests ->
                                requests
                                .requestMatchers("/users/signup", "/users/login", "/users/logout", "/users/validate-token")
                                .permitAll().anyRequest().authenticated())
                    .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

}
