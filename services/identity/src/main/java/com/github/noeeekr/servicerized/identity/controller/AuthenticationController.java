package com.github.noeeekr.servicerized.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.identity.common.logging.Debugger;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponse;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseError;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping(AuthenticationController.CONTROLLER_PATH)
@AllArgsConstructor
public class AuthenticationController extends Controller {
    public static final String CONTROLLER_PATH = "/api/auth/user";
    public static final String CONTROLLER_SIGNUP_PATH = "/signup";
    public static final String CONTROLLER_SIGNUP_CONFIRMATION_PATH = CONTROLLER_SIGNUP_PATH + "/confirmation";

    public static final String QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN = "emailConfirmationToken";
    
    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping
    @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_PATH)
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

    @GetMapping
    @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH + "/{confirmationToken}")
    public ResponseEntity<ClientResponse> signUpConfirmation() {
        
        return new ResponseEntity<>(new ClientResponse(new ClientResponseError("Recurso em construção. ", false)), HttpStatus.NOT_IMPLEMENTED);
    }
}
