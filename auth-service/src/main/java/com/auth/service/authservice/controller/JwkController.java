package com.auth.service.authservice.controller;

import com.auth.service.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class JwkController {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> getJwks() {
        try {
            // Get public key using JwtUtil
            PublicKey pubKey = jwtUtil.getPublicKey();
            
            if (!(pubKey instanceof RSAPublicKey)) {
                throw new RuntimeException("Public key is not an RSA key");
            }
            
            RSAPublicKey rsaPublicKey = (RSAPublicKey) pubKey;
            
            // Extract modulus and exponent from RSA public key
            String modulus = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(rsaPublicKey.getModulus().toByteArray());
            
            String exponent = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(rsaPublicKey.getPublicExponent().toByteArray());
            
            Map<String, Object> jwk = new HashMap<>();
            jwk.put("kty", "RSA");                    // Key type
            jwk.put("use", "sig");                    // Use for signature
            jwk.put("kid", "auth-service-key");       // Key ID
            jwk.put("alg", "RS256");                  // Algorithm
            jwk.put("n", modulus);                    // Modulus
            jwk.put("e", exponent);                   // Exponent
            
            Map<String, Object> jwks = new HashMap<>();
            jwks.put("keys", new Object[]{jwk});
            
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "public, max-age=3600") // Cache for 1 hour
                    .body(jwks);
            
        } catch (Exception e) {
            log.error("JWKS generation failed", e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error generating JWK: " + e.getMessage()));
        }
    }
    
    // Alternative endpoint for backward compatibility
    @GetMapping("/jwks")
    public ResponseEntity<Map<String, Object>> getJwksAlternative() {
        return getJwks();
    }
}
