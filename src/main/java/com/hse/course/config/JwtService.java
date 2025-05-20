package com.hse.course.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public JwtService(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration
    ) {
        this.secretKey = secretKey;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        log.info("JWT Service initialized");
        log.info("Secret key length: {}", secretKey.length());
        try {
            byte[] decoded = Decoders.BASE64.decode(secretKey);
            log.info("Base64 decode successful. Key length: {}", decoded.length);
        } catch (Exception e) {
            log.error("Failed to decode secret key", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        System.out.println("JWT Service: Generating token for user: " + userDetails.getUsername());
        return buildToken(new HashMap<>(), userDetails, accessTokenExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        try {
            if (userDetails == null || userDetails.getUsername() == null) {
                log.error("UserDetails или username равен null");
                throw new IllegalArgumentException("UserDetails или username равен null");
            }

            log.info("Building token for user: {}", userDetails.getUsername());
            log.debug("Extra claims: {}", extraClaims);
            log.debug("Token expiration: {} ms", expiration);

            Key signingKey = getSignInKey();
            log.debug("Signing key generated");

            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(signingKey, SignatureAlgorithm.HS256)
                    .compact();

        } catch (Exception e) {
            log.error("Ошибка при построении токена", e);
            throw e;
        }
    }

    public String extractEmail(String token) {
        return extractUsername(token);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        log.debug("Decoding secret key...");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        log.debug("Secret key decoded successfully. Length: {}", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}