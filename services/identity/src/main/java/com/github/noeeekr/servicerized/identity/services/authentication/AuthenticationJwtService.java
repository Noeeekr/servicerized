package com.github.noeeekr.servicerized.identity.services.authentication;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.repository.dto.internal.InternalUserDto;

@Service
public class AuthenticationJwtService extends JwtService {
    public String createToken(InternalUserDto user) throws JsonProcessingException {
        return this.buildToken(new ObjectMapper().writeValueAsString(user));
    }

    public boolean isTokenValid(String token, InternalUserDto user) {
        try {
            this._getPayload(token);
            return !this.isTokenExpired(token);
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public InternalUserDto getPayload(String token) {
        try {
            return this._getPayload(token);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private InternalUserDto _getPayload(String token) throws JsonProcessingException {
        return new ObjectMapper().readValue(token, InternalUserDto.class);
    }
}
