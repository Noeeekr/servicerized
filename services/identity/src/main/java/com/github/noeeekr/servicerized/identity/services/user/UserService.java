package com.github.noeeekr.servicerized.identity.services.user;

import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import com.github.noeeekr.servicerized.identity.common.configuration.ServerConfiguration;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.controller.AuthenticationController;
import com.github.noeeekr.servicerized.identity.repository.dao.UserDao;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;
import com.github.noeeekr.servicerized.identity.services.configuration.ServiceConfiguration;
import com.github.noeeekr.servicerized.identity.services.notification.NotificationService;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserDao dao;

    @Autowired
    private NotificationService notificationService;

    public Response<User> createUser(User user) {
        return this.createUser(user, new ServiceConfiguration());
    }

    /**
     * @param user The object to be created
     * @return A Response object containing the created user on success. Otherwise, a Failure object
     *         that may contain the following: UnhandledException (exception on credentials
     *         validation), ResourceFound (credentials already taken), FailedPersit (exception on
     *         while creating user)
     */
    public Response<User> createUser(User user, ServiceConfiguration configuration) {
        // Section : Attempt To Get User With Required Credentials.
        Response<User> response = dao.getByEmail(user.getEmail());
        if (response.isSuccess() == false)
            return response;

        // Section : Validate If Credentials are Available.
        User target = response.getPayload();
        if (target != null)
            return this.handleUserAlreadyExists();

        // Section : Create User With Required Credentials.
        response = dao.saveOne(user, configuration);
        if (response.isSuccess() == false)
            return response;

        // Section : Create User Email Confirmation Token.
        UserEmailConfirmation confirmation = new UserEmailConfirmation().setUser(user);
        {
            Response<UserEmailConfirmation> saveEmailResponse =
                    dao.saveEmailConfirmation(confirmation, configuration);
            if (saveEmailResponse.isSuccess() == false)
                return response.fail(saveEmailResponse.getFailure());
        }

        // Section : Send User Email Confirmation Token.
        {
            URI endpoint = createEmailConfirmationUri(confirmation.getToken());
            Response<?> sendConfirmationResponse =
                    notificationService.sendConfirmation(user.getEmail(), user.getName(), endpoint);
            if (sendConfirmationResponse.isSuccess() == false)
                return response.fail(sendConfirmationResponse.getFailure());
        }

        return Response.<User>builder().success(user).build();
    }

    protected URI createEmailConfirmationUri(UUID confirmationToken) {
        String domain = String.format("https://%s", ServerConfiguration.getDomain()) + "";
        return UriComponentsBuilder.fromUriString(domain)
                .path(AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH)
                .queryParam(AuthenticationController.QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN,
                        confirmationToken)
                .build().toUri();
    }

    protected Response<User> handleUserAlreadyExists() {
        Failure failure =
                new UserFailure.ResourceFound("O nome de usuário ou e-mail já foram escolhidos. ");
        return Response.<User>builder().fail(failure).build();
    }
}
