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

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        Claims claims = claimsJws.getPayload();
        String userId = getRequiredClaim(claims, JWTClaims.USER_ID);
        String email = getRequiredClaim(claims, JWTClaims.EMAIL);
        String role =  getRequiredClaim(claims, JWTClaims.ROLE);

        List<String> permissions = getRequiredListClaim(claims, JWTClaims.PERMISSIONS);

       return new AuthenticatedUser(userId,email,role,permissions);
    }

    private static String getRequiredClaim(Claims claims, String claimName) {
        String value = claims.get(claimName, String.class);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }

    private static List<String> getRequiredListClaim(Claims claims, String claimName) {
        List<String> value = (List<String>) claims.get(claimName);
        if(value == null){
            throw new IllegalArgumentException("Missing required claim: " + claimName);
        }
        return value;
    }
}
