package com.github.noeeekr.servicerized.identity.services.user;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import com.github.noeeekr.servicerized.identity.common.configuration.ServerConfiguration;
import com.github.noeeekr.servicerized.identity.common.logging.Debugger;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.common.response.failure.UserFailure;
import com.github.noeeekr.servicerized.identity.controller.AuthenticationController;
import com.github.noeeekr.servicerized.identity.repository.dao.GroupDao;
import com.github.noeeekr.servicerized.identity.repository.dao.UserDao;
import com.github.noeeekr.servicerized.identity.repository.dao.UserEmailConfirmationDao;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;
import com.github.noeeekr.servicerized.identity.services.configuration.ServiceConfiguration;
import com.github.noeeekr.servicerized.identity.services.notification.NotificationService;
import com.github.noeeekr.servicerized.identity.services.user.request.UserSignInInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserDao dao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserEmailConfirmationDao emailConfirmationDao;

    @Autowired
    private NotificationService notificationService;

    public Response<User> createUser(User user) {
        return this.createUser(user, new ServiceConfiguration());
    }

    public Response<?> validateEmail(UUID emailConfirmationToken) {
        Response<?> response = emailConfirmationDao.validateUserEmail(emailConfirmationToken);
        return response;
    }

    public Response<User> getBySignInCredentials(UserSignInInterface request) {
        Response<User> response = groupDao.getBySigninCredentials(request);
        return response;
    }

    public Response<List<UserEmailConfirmation>> getEmailConfirmations(String email) {
        Response<List<UserEmailConfirmation>> response = dao.getEmailConfirmations(email);
        return response;
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
                    emailConfirmationDao.saveEmailConfirmation(confirmation, configuration);
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
        URI uri = UriComponentsBuilder.fromUriString(ServerConfiguration.getDomain() + "")
                .path(AuthenticationController.CONTROLLER_SIGNUP_CONFIRMATION_PATH)
                .queryParam(AuthenticationController.QUERY_PARAM_EMAIL_CONFIRMATION_TOKEN,
                        confirmationToken)
                .build().toUri();
        if (log.isDebugEnabled()) {
            log.debug("%s%s", Debugger.formatDomain(UserService.class.getName(),
                    "Created email confirmation", "URI"), uri);
        }
        return uri;
    }

    protected Response<User> handleUserAlreadyExists() {
        Failure failure =
                new UserFailure.ResourceFound("O nome de usuário ou e-mail já foram escolhidos. ");
        return Response.<User>builder().fail(failure).build();
    }
}
