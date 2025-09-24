package com.auth.service.authservice.controller;

import com.auth.service.authservice.dto.LoginRequestDTO;
import com.auth.service.authservice.dto.LoginResponseDTO;
import com.auth.service.authservice.dto.RegisterRequestDTO;
import com.auth.service.authservice.dto.RegisterResponseDTO;
import com.auth.service.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    // REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            RegisterResponseDTO response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Registration failed: " + e.getMessage());
        }
    }

    // TEST ENDPOINT - Check if authentication is working
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated user: " + authentication.getName() + 
                                   " with authorities: " + authentication.getAuthorities());
        }
        return ResponseEntity.status(401).body("Not authenticated");
    }

    // VALIDATE TOKEN ENDPOINT (for other microservices)
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            boolean isValid = authService.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token validation failed: " + e.getMessage());
        }
    }
}
