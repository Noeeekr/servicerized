package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.identity.common.logging.Debugger;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponse;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("/api/auth/user")
@AllArgsConstructor
public class AuthenticationController extends Controller {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping
    @RequestMapping("/signup")
    public ResponseEntity<ClientResponse> signUp(@RequestBody SignUpRequest request) {
        Response<User> serviceResponse = authenticationService.newUser(request);

        if (serviceResponse.isSuccess() == false) {
            Failure failure = serviceResponse.getFailure();
            return this.handleFailure(failure, AuthenticationController.class.getName(),
                    "Signup Endpoint");
        }

        User user = serviceResponse.getPayload();

        if (log.isDebugEnabled()) {
            Debugger.displayEntity("Created User", user, Debugger.class.getName(),
                    AuthenticationController.class.getName(), "Signup Endpoint");
        }

        return new ResponseEntity<>(new ClientResponse(user), HttpStatus.CREATED);
    }
}
