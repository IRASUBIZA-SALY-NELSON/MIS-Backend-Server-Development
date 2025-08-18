package com.rca.mis.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT token operations
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Value("${app.jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * Generate access token for user
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS");
        return createToken(claims, userDetails.getUsername(), expiration);
    }

    /**
     * Generate refresh token for user
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "REFRESH");
        return createToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    /**
     * Create JWT token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Get signing key from secret
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token");
        }
    }

    /**
     * Check if token is expired
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Validate token for user
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is a refresh token
     */
    public Boolean isRefreshToken(String token) {
        try {
            String tokenType = extractClaim(token, claims -> claims.get("type", String.class));
            return "REFRESH".equals(tokenType);
        } catch (Exception e) {
            log.error("Error checking token type: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is an access token
     */
    public Boolean isAccessToken(String token) {
        try {
            String tokenType = extractClaim(token, claims -> claims.get("type", String.class));
            return "ACCESS".equals(tokenType);
        } catch (Exception e) {
            log.error("Error checking token type: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get token expiration time in milliseconds
     */
    public Long getTokenExpirationTime(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            log.error("Error getting token expiration time: {}", e.getMessage());
            return 0L;
        }
    }

    /**
     * Get token creation time
     */
    public Date getTokenCreationTime(String token) {
        try {
            return extractClaim(token, Claims::getIssuedAt);
        } catch (Exception e) {
            log.error("Error getting token creation time: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Check if token will expire soon (within specified minutes)
     */
    public Boolean isTokenExpiringSoon(String token, int minutesThreshold) {
        try {
            long expirationTime = getTokenExpirationTime(token);
            return expirationTime > 0 && expirationTime < (minutesThreshold * 60 * 1000);
        } catch (Exception e) {
            log.error("Error checking if token expires soon: {}", e.getMessage());
            return false;
        }
    }
}
