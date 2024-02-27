package dev.batuhanyetgin.ms_security.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenManager {


    private final Date currenTime = new Date(System.currentTimeMillis());
    @Value("${jwtdemo.validity}")
    private String validity;
    @Value("${jwtdemo.secretKey}")
    private String secretKey;

    public String generateToken(String email, String userId) {
        return Jwts.builder()
                .subject(email)
                .issuer(userId)
                .issuedAt(currenTime)
                .expiration(new Date(System.currentTimeMillis() + Integer.parseInt(validity)))
                .signWith(getSecretKey())
                .compact();

    }

    public boolean validateToken(String token) {
        return getEmail(token) != null && !isExpired(token);
    }

    public String getEmail(String token) {
        return parseClaim(token, Claims::getSubject);
    }

    private boolean isExpired(String token) {
        return parseClaim(token, Claims::getExpiration).before(currenTime);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private <T> T parseClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsTFunction.apply(claims);
    }

}
