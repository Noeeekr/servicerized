package com.github.noeeekr.servicerized.identity.services.authentication;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public abstract class JwtService {
    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMilisseconds;

    public String generateToken(Object payload) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(payload);
        return this.buildToken(json);
    };

    protected String buildToken(String payload) {
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(payload);
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date(System.currentTimeMillis() + this.expirationMilisseconds));
        builder.signWith(getSignInKey());
        return builder.compact();
    }

    protected boolean isTokenExpired(String token) {
        return this.extractClaim(token, claims -> claims.getExpiration()).before(new Date());
    }

    protected <T> T getPayload(String token, Class<T> targetClass)
            throws JsonProcessingException, JsonMappingException {
        String json = this.extractClaim(token, claims -> claims.getSubject());
        return new ObjectMapper().readValue(json, targetClass);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = this.extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        return parser.parseClaimsJws(token).getBody();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
