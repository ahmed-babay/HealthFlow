package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.LoginRequestDTO;
import com.patient.service.patientservice.dto.LoginResponseDTO;
import com.patient.service.patientservice.dto.RegisterRequestDTO;
import com.patient.service.patientservice.dto.RegisterResponseDTO;
import com.patient.service.patientservice.model.User;
import com.patient.service.patientservice.repository.UserRepository;
import com.patient.service.patientservice.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // 1. AUTHENTICATE USER
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // 2. GET USER DETAILS
            User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsername(), 
                loginRequest.getUsername()
            ).orElseThrow(() -> new RuntimeException("User not found"));

            // 3. GENERATE JWT TOKEN
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            // 4. RETURN TOKEN AND USER INFO
            LoginResponseDTO response = new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName()
            );

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
            // 1. CHECK IF USER ALREADY EXISTS
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.status(400).body("Username already exists");
            }
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.status(400).body("Email already exists");
            }

            // 2. CREATE NEW USER
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // ENCRYPT PASSWORD!
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            
            // Set role (default to PATIENT, or from request)
            if (registerRequest.getRole() != null) {
                user.setRole(User.Role.valueOf(registerRequest.getRole().toUpperCase()));
            } else {
                user.setRole(User.Role.PATIENT); // Default role
            }

            // 3. SAVE USER
            User savedUser = userRepository.save(user);

            // 4. GENERATE TOKEN FOR IMMEDIATE LOGIN
            String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());

            // 5. RETURN SUCCESS RESPONSE
            RegisterResponseDTO response = new RegisterResponseDTO(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                token
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
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
}
