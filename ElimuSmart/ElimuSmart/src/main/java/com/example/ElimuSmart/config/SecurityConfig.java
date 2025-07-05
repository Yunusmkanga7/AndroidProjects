package com.example.ElimuSmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Ruhusu CORS
                .csrf(csrf -> csrf.disable())    // Zima CSRF kwa development
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Ruhusu requests zote
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // CORS Global config
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // RUKSA address zote
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
