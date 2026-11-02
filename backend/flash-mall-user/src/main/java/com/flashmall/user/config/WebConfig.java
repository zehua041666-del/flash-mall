package com.flashmall.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.flashmall.user.interceptor.JwtInterceptor;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor 
@Configuration //注明这是一个配置类 
public class WebConfig implements WebMvcConfigurer{
    private final JwtInterceptor jwtInterceptor;

    @Override 
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/user/login","/user/register")
        .excludePathPatterns("/product/**")
        .excludePathPatterns("/error");
    }
    
}
