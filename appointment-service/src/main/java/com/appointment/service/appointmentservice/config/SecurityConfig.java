package com.appointment.service.appointmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Allow health check and H2 console without authentication
                .requestMatchers("/api/appointments/health", "/h2-console/**").permitAll()
                
                // APPOINTMENT endpoints - role-based access
                .requestMatchers("GET", "/api/appointments/patient/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                .requestMatchers("GET", "/api/appointments/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers("GET", "/api/appointments").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers("POST", "/api/appointments/**").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers("PUT", "/api/appointments/**").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers("DELETE", "/api/appointments/**").hasAnyRole("DOCTOR", "ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
            .headers(headers -> headers.frameOptions().disable()); // For H2 console

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

