package com.nibble.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtService {

    private final String secretKey;
    private final long expirationMs;

    public JwtService(String secretKey, long expirationMs) {
        this.secretKey = secretKey;
        this.expirationMs = expirationMs;
    }

    public String generateToken(TokenRequest request) {
        final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        final long now = Instant.now().toEpochMilli();
        
        return Jwts.builder()
                .claim(JWTClaims.USER_ID, request.userId())
                .claim(JWTClaims.EMAIL, request.email())
                .claim(JWTClaims.ROLE, request.role())
                .claim(JWTClaims.PERMISSIONS, request.permissions())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    public AuthenticatedUser validateToken(String token) {
        final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        final Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        final Claims claims = claimsJws.getPayload();
        final String userId = getRequiredClaim(claims, JWTClaims.USER_ID);
        final String email = getRequiredClaim(claims, JWTClaims.EMAIL);
        final String role =  getRequiredClaim(claims, JWTClaims.ROLE);

        final List<String> permissions = getRequiredListClaim(claims, JWTClaims.PERMISSIONS);

       return new AuthenticatedUser(userId,email,role,permissions);
    }

    private String getRequiredClaim(Claims claims, String claimName) {
        final String value = claims.get(claimName, String.class);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }

    private List<String> getRequiredListClaim(Claims claims, String claimName) {
        final List<String> value = (List<String>) claims.get(claimName);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }
}
