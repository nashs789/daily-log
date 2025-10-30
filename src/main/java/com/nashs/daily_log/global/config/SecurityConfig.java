package com.nashs.daily_log.global.config;

import com.nashs.daily_log.domain.auth.handler.OAuth2FailureHandler;
import com.nashs.daily_log.domain.auth.handler.OAuth2SuccessHandler;
import com.nashs.daily_log.domain.auth.props.AuthCookieProps;
import com.nashs.daily_log.domain.auth.service.GoogleOAuth2UserService;
import com.nashs.daily_log.domain.auth.service.JwtService;
import com.nashs.daily_log.global.filter.JwtCookieAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final GoogleOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService, AuthCookieProps authCookieProps) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/login", "/oauth2/**",
                                     "/css/**","/js/**","/images/**","/webjars/**").permitAll()
                    .anyRequest().permitAll()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
            )
            .oauth2Login(oauth -> oauth
                    .loginPage("/login")
                    .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
            )
            .logout(l -> l.logoutSuccessUrl("/")
                          .deleteCookies(authCookieProps.accessName(), authCookieProps.refreshName())
                          .invalidateHttpSession(true)
                          .clearAuthentication(true))
            .addFilterBefore(new JwtCookieAuthFilter(jwtService, authCookieProps.accessName()),
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
