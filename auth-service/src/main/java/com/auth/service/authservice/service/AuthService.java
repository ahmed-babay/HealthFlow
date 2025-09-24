package com.auth.service.authservice.service;

import com.auth.service.authservice.dto.LoginRequestDTO;
import com.auth.service.authservice.dto.LoginResponseDTO;
import com.auth.service.authservice.dto.RegisterRequestDTO;
import com.auth.service.authservice.dto.RegisterResponseDTO;
import com.auth.service.authservice.model.User;
import com.auth.service.authservice.repository.UserRepository;
import com.auth.service.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // LOGIN SERVICE
    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
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
        return new LoginResponseDTO(
            token,
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.getFirstName(),
            user.getLastName()
        );
    }

    // REGISTER SERVICE
    public RegisterResponseDTO register(RegisterRequestDTO registerRequest) {
        // 1. CHECK IF USER ALREADY EXISTS
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
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
        return new RegisterResponseDTO(
            savedUser.getId().toString(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            token
        );
    }

    // VALIDATE TOKEN SERVICE
    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }

    // GET USER FROM TOKEN
    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    // GET ROLE FROM TOKEN
    public String getRoleFromToken(String token) {
        return jwtUtil.extractRole(token);
    }
}

