package com.github.noeeekr.servicerized.authorization.cookie;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthorizationCookieService extends JwtService {
    public String createToken(UUID userId) throws JsonProcessingException {
        return this.buildToken(new ObjectMapper()
                .writeValueAsString(new AuthorizationToken(userId, this.version())));
    }

    public boolean isTokenValid(String token, UUID userId) {
        try {
            this._getPayload(token);
            return !this.isTokenExpired(token);
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public AuthorizationToken getPayload(String token) {
        try {
            return this._getPayload(token);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private AuthorizationToken _getPayload(String token) throws JsonProcessingException {
        return new ObjectMapper().readValue(token, AuthorizationToken.class);
    }
}
