package com.jason.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // allow all /api endpoints
                        .allowedOrigins("https://13-game.netlify.app") // your Netlify URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // what methods you want to allow
                        .allowedHeaders("*"); // allow all headers
            }
        };
    }
}