package com.github.noeeekr.servicerized.identity.controller;

import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.noeeekr.servicerized.identity.common.logging.Debugger;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failures;
import com.github.noeeekr.servicerized.identity.controller.request.SignInRequest;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.controller.response.ClientResponse;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationJwtService;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping(AuthenticationController.CONTROLLER_PATH)
@RequiredArgsConstructor
public class AuthenticationController extends Controller {
    public static final String CONTROLLER_PATH = "/api/auth/account";
    public static final String CONTROLLER_SIGNUP_PATH = "/signup";
    public static final String CONTROLLER_SIGNIN_PATH = "/signin";
    public static final String CONTROLLER_SIGNUP_CONFIRMATION_PATH =
            CONTROLLER_SIGNUP_PATH + "/confirmation";

    public static final String QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN = "emailConfirmationToken";

    private final AuthenticationService authenticationService;
    private final AuthenticationJwtService authenticationJwtService;

    @PostMapping
    @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_PATH)
    public ResponseEntity<ClientResponse> signUp(@RequestBody SignUpRequest request) {
        Response<User> serviceResponse = authenticationService.createUserAccount(request);

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
    @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH)
    public ResponseEntity<ClientResponse> signUpConfirmation(@RequestParam(
            name = AuthenticationController.QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN) UUID confirmationToken) {
        Response<?> response = authenticationService.authorizeUserAccount(confirmationToken);
        if (response.isSuccess() == false)
            return this.handleFailure(response.getFailure(),
                    AuthenticationController.class.getName(), "Signup Confirmation Endpoint");

        return new ResponseEntity<>(new ClientResponse(ClientResponse.getEmptyPayload()),
                HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_PATH)
    public ResponseEntity<ClientResponse> signIn(@RequestBody SignInRequest requestBody) {
        Response<User> response = authenticationService.signUserAccount(requestBody);
        if (response.isSuccess() == false)
            return this.handleFailure(response.getFailure(),
                    AuthenticationController.class.getName(), "Signin Endpoint");

        String cookie;
        try {
            cookie = authenticationJwtService.createToken(response.getPayload());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse user details to json. " + e.getMessage());
            return this.handleFailure(new Failures.UnhandledException(e),
                    AuthenticationController.class.getName(), "Signin Endpoint");
        }

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie)
                .body(new ClientResponse(response.getPayload()));
    }
}
