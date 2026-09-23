package com.github.noeeekr.servicerized.product.service;

import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationCookieService;

@Service
public class AuthorizationService extends AuthorizationCookieService {
    public AuthorizationService() {
        super();
    }
}
