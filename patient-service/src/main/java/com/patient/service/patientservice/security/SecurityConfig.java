package com.patient.service.patientservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true) // Enable method-level security
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Allow health check and H2 console without authentication
                .requestMatchers("/patients/health", "/h2-console/**").permitAll()
                
                // PATIENT endpoints - only DOCTOR and ADMIN can manage patients
                .requestMatchers("/patients").hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers("/patients/**").hasAnyRole("DOCTOR", "ADMIN")
                
                // MEDICAL RECORDS - only DOCTOR and ADMIN can access
                .requestMatchers("/medical-records/**").hasAnyRole("DOCTOR", "ADMIN")
                
                // ALLERGIES - only DOCTOR and ADMIN can manage
                .requestMatchers("/allergies/**").hasAnyRole("DOCTOR", "ADMIN")
                
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

/*
SECURITY RULES EXPLAINED:

1. PUBLIC ENDPOINTS (/auth/**):
   - Anyone can access login/register
   - No token required

2. PROTECTED ENDPOINTS:
   - /patients/** → Only DOCTOR or ADMIN roles
   - /medical-records/** → Only DOCTOR or ADMIN roles
   - Everything else → Any authenticated user

3. ROLES:
   - PATIENT: Can view their own records (we'll implement this later)
   - DOCTOR: Can manage patients and medical records
   - ADMIN: Can do everything + manage users

4. STATELESS:
   - No server-side sessions
   - All authentication info is in the JWT token
   - Perfect for microservices!
*/
