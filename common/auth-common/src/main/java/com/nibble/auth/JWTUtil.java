package com.nibble.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.List;

public  class JWTUtil {

    public static AuthenticatedUser validateToken(String token, String secretKey){

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

    private static String getRequiredClaim(Claims claims, String claimName) {
        final String value = claims.get(claimName, String.class);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }

    private static List<String> getRequiredListClaim(Claims claims, String claimName) {
        final List<String> value = (List<String>) claims.get(claimName);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }
}
