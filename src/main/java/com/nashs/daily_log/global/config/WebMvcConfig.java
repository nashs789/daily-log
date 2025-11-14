package com.nashs.daily_log.global.config;

import com.nashs.daily_log.global.interceptor.LoginCheckInterceptor;
import com.nashs.daily_log.global.provider.LifeLogUserProvider;
import com.nashs.daily_log.global.resolver.LifeLogUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LifeLogUserProvider lifeLogUserProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LifeLogUserArgumentResolver(lifeLogUserProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor(lifeLogUserProvider))
                .addPathPatterns("/api/**");
    }
}
