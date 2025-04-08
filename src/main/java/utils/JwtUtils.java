package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class JwtUtils {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode("kTBbGeYc0sOz7qO/N4NFDYFvPZgI7o5T1Hr2zRnXghY="));
    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
