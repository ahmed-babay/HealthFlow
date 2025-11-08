package com.auth.service.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;
    
    @Value("${jwt.public-key-path}")
    private String publicKeyPath;
    
    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    private PrivateKey getPrivateKey() {
        try {
            ClassPathResource resource = new ClassPathResource(privateKeyPath.replace("classpath:", ""));
            String keyContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            
            // Remove PEM headers and whitespace
            keyContent = keyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                                 .replace("-----END PRIVATE KEY-----", "")
                                 .replaceAll("\\s", "");
            
            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key from file", e);
        }
    }

    public PublicKey getPublicKey() {
        try {
            String path = publicKeyPath.replace("classpath:", "");
            ClassPathResource resource = new ClassPathResource(path);

            try (InputStream is = resource.getInputStream()) {
                String keyPem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                String keyContent = keyPem
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");

                byte[] keyBytes = Base64.getDecoder().decode(keyContent);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePublic(spec);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading public key from file", e);
        }
    }

    // 1. GENERATE TOKEN - Create a JWT token for a user
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Add user role to token
        return createToken(claims, username);
    }
    
    // GENERATE TOKEN WITH USER ID
    public String generateToken(String username, String role, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        return createToken(claims, username);
    }

    // Helper method to create the actual token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)                           // Custom data (role)
                .subject(subject)                         // Username
                .issuer("http://localhost:4002/auth")     // Issuer claim (REQUIRED for OAuth2 Resource Server)
                .issuedAt(new Date(System.currentTimeMillis()))  // Token creation time
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Expiration time
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)  // Sign with private key using RSA
                .compact();                              // Convert to string
    }

    // 2. EXTRACT USERNAME - Get username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. EXTRACT ROLE - Get user role from token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // 4. EXTRACT EXPIRATION - Get when token expires
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // 5. CHECK IF TOKEN IS EXPIRED
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 6. VALIDATE TOKEN - Check if token is valid for a user
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 7. CHECK IF TOKEN IS VALID (without username check)
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}

