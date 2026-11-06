package com.pastelaria.familia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Libera as origens do front-end (ajuste conforme necessário, ou use List.of("*") para desenvolvimento)
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:5500"));

        // Libera os métodos HTTP padrão
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Libera os headers necessários (incluindo o nosso header customizado de admin e o Authorization)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Admin-Email"));

        // Permite credenciais (cookies, headers de autenticação)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}