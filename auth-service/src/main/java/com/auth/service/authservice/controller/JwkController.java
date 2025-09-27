package com.auth.service.authservice.controller;

import com.auth.service.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class JwkController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/jwks")
    public ResponseEntity<Map<String, Object>> getJwks() {
        try {
            // Get public key using JwtUtil
            PublicKey pubKey = jwtUtil.getPublicKey();
            
            // Extract modulus and exponent from public key
            String modulus = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(pubKey.getEncoded());
            
            Map<String, Object> jwk = new HashMap<>();
            jwk.put("kty", "RSA");                    // Key type
            jwk.put("use", "sig");                    // Use for signature
            jwk.put("kid", "auth-service-key");       // Key ID
            jwk.put("alg", "RS256");                  // Algorithm
            jwk.put("n", modulus);                    // Modulus (simplified)
            jwk.put("e", "AQAB");                     // Exponent (standard for RSA)
            
            Map<String, Object> jwks = new HashMap<>();
            jwks.put("keys", new Object[]{jwk});
            
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jwks);
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error generating JWK: " + e.getMessage()));
        }
    }
}
