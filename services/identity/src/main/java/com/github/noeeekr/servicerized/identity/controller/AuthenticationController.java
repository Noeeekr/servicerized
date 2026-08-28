package com.github.noeeekr.servicerized.identity.controller;

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
import com.github.noeeekr.servicerized.identity.repository.dto.UserDto;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.AuthenticationService;
import com.github.noeeekr.servicerized.identity.services.UserService;
import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("/api/auth/user")
@AllArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private final UserService userService;

    @GetMapping
    @RequestMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest request) {
        Response<User> response = userService.newUser(UserDto.fromCreateRequest(request));
        if (response.isSuccess() == false) {
            String message = response.getFailure().getClientSafeMessage();
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Tables: User; Group; GroupUsers;

        // get user info
        // validate user info
        ///
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
