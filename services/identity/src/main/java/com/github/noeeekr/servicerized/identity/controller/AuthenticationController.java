package com.github.noeeekr.servicerized.identity.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponse;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseError;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseErrorDto;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("/api/auth/user")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping
    @RequestMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest request) {
        Response<User> serviceResponse = authenticationService.newUser(request);
        if (serviceResponse.isSuccess() == false) {
            Failure failure = serviceResponse.getFailure();
            log.error("Authentication Controller: Signup Endpoint: Failure: " + failure.message());
            if (failure.error() != null) {
                failure.error().printStackTrace();
            }
            
            ClientResponseError clientResponseError = ClientResponseErrorDto.fromFailure(failure);
            ClientResponse clientResponse = new ClientResponse(clientResponseError);
            HttpStatus statusCode = Objects.requireNonNull(failure.code());

            return new ResponseEntity<>(clientResponse, statusCode);
        }

        User user = serviceResponse.getPayload();

        try {
            log.debug("Authentication Controller: Signup Endpoint: Created User: "
                    + new ObjectMapper().writeValueAsString(user));
        } catch (Exception e) {
            log.debug(
                    "Authentication Controller: Signup Endpoint: Unable to create JSON view of entity 'user'.\n "
                            + e.getMessage());
        }

        return new ResponseEntity<>(new ClientResponse(user), HttpStatus.CREATED);
    }

}
