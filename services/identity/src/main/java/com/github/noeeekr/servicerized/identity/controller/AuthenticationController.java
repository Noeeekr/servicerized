package com.github.noeeekr.servicerized.identity.controller;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.controller.Controller;
import com.github.noeeekr.servicerized.identity.controller.request.SignInRequest;
import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.repository.dto.internal.InternalUserDto;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationService;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.client.ClientResponse;
import com.github.noeeekr.servicerized.response.failure.Failure;
import com.github.noeeekr.servicerized.response.failure.Failures;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationCookieService;
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

        public static final String CONTROLLER_AUTH_COOKIE_NAME = "auth";

        @Value("${app.jwt.expiration-ms}")
        private long expirationMilisseconds;

        private final AuthenticationService authenticationService;
        private final AuthorizationCookieService authorizationCookieService;

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
                        DebugLogger.displayEntity("Created User", new InternalUserDto(user),
                                        AuthenticationController.class.getName(),
                                        "Signup Endpoint");
                }

                return new ResponseEntity<>(new ClientResponse(user), HttpStatus.CREATED);
        }

        @GetMapping
        @RequestMapping(AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH)
        public ResponseEntity<ClientResponse> signUpConfirmation(@RequestParam(
                        name = AuthenticationController.QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN) UUID confirmationToken) {
                Response<?> response =
                                authenticationService.authorizeUserAccount(confirmationToken);
                if (response.isSuccess() == false)
                        return this.handleFailure(response.getFailure(),
                                        AuthenticationController.class.getName(),
                                        "Signup Confirmation Endpoint");

                return new ResponseEntity<>(new ClientResponse(ClientResponse.getEmptyPayload()),
                                HttpStatus.OK);
        }

        @PostMapping
        @RequestMapping(AuthenticationController.CONTROLLER_SIGNIN_PATH)
        public ResponseEntity<ClientResponse> signIn(@RequestBody SignInRequest requestBody) {
                Response<User> response = authenticationService.signUserAccount(requestBody);
                if (response.isSuccess() == false)
                        return this.handleFailure(response.getFailure(),
                                        AuthenticationController.class.getName(),
                                        "Signin Endpoint");

                String cookieContent;
                try {
                        cookieContent = authorizationCookieService
                                        .createToken(response.getPayload().getUserId());
                } catch (JsonProcessingException e) {
                        log.error("Failed to parse user details to json. " + e.getMessage());
                        return this.handleFailure(new Failures.UnhandledException(e),
                                        AuthenticationController.class.getName(),
                                        "Signin Endpoint");
                }

                Duration duration = Duration.ofMillis(expirationMilisseconds);
                Objects.requireNonNull(duration, "Duration cannot be null. ");

                ResponseCookie cookie =
                                ResponseCookie.from(CONTROLLER_AUTH_COOKIE_NAME, "" + cookieContent)
                                                .httpOnly(true).secure(true).path("/")
                                                .maxAge(duration).sameSite("Lax").build();

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(new ClientResponse(response.getPayload()));
        }
}
