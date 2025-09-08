package com.patient.service.patientservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                   @NonNull HttpServletResponse response, 
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. GET TOKEN FROM REQUEST HEADER
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if Authorization header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtUtil.extractUsername(jwt); // Extract username from token
            } catch (Exception e) {
                System.err.println("JWT token is invalid: " + e.getMessage());
            }
        }

        // 2. VALIDATE TOKEN AND SET AUTHENTICATION
        // If we have a username and no one is currently authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token against the user
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    // Set additional details
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Tell Spring Security that this user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    
                    System.out.println("User authenticated: " + username + " with role: " + 
                                     userDetails.getAuthorities());
                }
            } catch (Exception e) {
                System.err.println("Authentication failed for user: " + username + " - " + e.getMessage());
            }
        }

        // 3. CONTINUE WITH THE REQUEST
        // Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    // Skip authentication for certain endpoints (like login)
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Don't filter these paths (allow access without token)
        return path.equals("/auth/login") || 
               path.equals("/auth/register") ||
               path.startsWith("/h2-console") ||
               path.equals("/") ||
               path.equals("/error");
    }
}
