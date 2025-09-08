package com.patient.service.patientservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enables @PreAuthorize annotations
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless JWT authentication)
            .csrf(csrf -> csrf.disable())
            
            // Configure session management (stateless for JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // PUBLIC ENDPOINTS (no authentication required)
                .requestMatchers("/auth/**").permitAll()           // Login/register
                .requestMatchers("/h2-console/**").permitAll()     // Database console (dev only)
                .requestMatchers("/error").permitAll()            // Error pages
                
                // PROTECTED ENDPOINTS (authentication required)
                .requestMatchers("/patients/**").hasAnyRole("DOCTOR", "ADMIN")  // Only doctors/admins can manage patients
                .requestMatchers("/medical-records/**").hasAnyRole("DOCTOR", "ADMIN") // Only doctors/admins can manage records
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Add our JWT filter before the default authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Configure headers for H2 console (development only)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    // Password encoder bean - encrypts passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strong encryption for passwords
    }

    // Authentication manager bean - manages authentication process
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
