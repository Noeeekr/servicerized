package com.github.noeeekr.servicerized.identity.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponse;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseError;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseErrorDTO;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.AuthenticationService;
import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("/api/auth/user")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @GetMapping
    @RequestMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest request) {
        Response<User> newUserResponse = authenticationService.newUser(request);

        // Handle errors
        if (newUserResponse.isSuccess() == false) {
            Failure failure = newUserResponse.getFailure();

            ClientResponseError err = ClientResponseErrorDTO.fromFailure(failure);
            ClientResponse response = new ClientResponse(err);
            HttpStatus code = Objects.requireNonNull(failure.code());

            return new ResponseEntity<>(response, code);
        }

        User user = newUserResponse.getPayload();
        // Tables: User; Group; GroupUsers;

        // get user info
        // validate user info
        // Return data
        new ClientResponse(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
