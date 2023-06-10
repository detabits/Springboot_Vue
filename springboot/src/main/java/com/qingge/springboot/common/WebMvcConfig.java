package com.qingge.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor())
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/user/login", "/api/user/register");
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
