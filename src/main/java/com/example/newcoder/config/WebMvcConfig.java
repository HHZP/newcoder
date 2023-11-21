package com.example.newcoder.config;

import com.example.newcoder.controller.interceptor.LoginRequiredInterceptor;
import com.example.newcoder.controller.interceptor.LoginTicketInterceptor;
import com.example.newcoder.controller.interceptor.MessageInterceptor;
import com.example.newcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor requiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpg");
//        registry.addInterceptor(requiredInterceptor)
//                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpg");
        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpg");
    }
}
