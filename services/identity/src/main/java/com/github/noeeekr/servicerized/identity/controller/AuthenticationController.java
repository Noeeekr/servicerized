package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.noeeekr.servicerized.identity.controller.models.SignUpRequest;
import com.github.noeeekr.servicerized.identity.services.AuthenticationService;

@RestController()
@RequestMapping("/api/auth/user")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    @RequestMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest body) {
        // Tables: User; Group; GroupUsers; 
        
        // get user info
        // validate user info
        ///
        return new ResponseEntity<>(HttpStatus.OK);
    }

}