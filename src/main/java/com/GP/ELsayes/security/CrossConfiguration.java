package com.GP.ELsayes.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Allow orders from any origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow the HTTP methods you need
                .allowedHeaders("*"); // Allow all headers
    }
}
