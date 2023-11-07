package com.example.newcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class helloconfig {

    @Bean
    public int solve(){
        return 4;
    }
}
