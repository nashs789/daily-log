package com.nashs.daily_log.global.config;

import com.nashs.daily_log.domain.login.handler.OAuth2FailureHandler;
import com.nashs.daily_log.domain.login.handler.OAuth2SuccessHandler;
import com.nashs.daily_log.domain.login.service.GoogleOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final GoogleOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/api/**").permitAll()
                    //.requestMatchers("/oauth2/**", "/login/**").permitAll()
                    .anyRequest().permitAll()
                    //.authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
            )
            .logout(l -> l.logoutSuccessUrl("/").permitAll());
        return http.build();
    }
}
