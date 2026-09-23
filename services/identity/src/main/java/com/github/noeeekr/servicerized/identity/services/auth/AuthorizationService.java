package com.github.noeeekr.servicerized.identity.services.auth;

import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationCookieService;

@Service
public class AuthorizationService extends AuthorizationCookieService {
    public AuthorizationService() {
        super();
    }
}
