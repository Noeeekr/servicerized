package com.github.noeeekr.servicerized.identity.services.authentication;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.repository.dto.GroupDto;
import com.github.noeeekr.servicerized.identity.repository.dto.UserDto;
import com.github.noeeekr.servicerized.identity.repository.models.Group;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.configuration.ServiceConfiguration;
import com.github.noeeekr.servicerized.identity.services.group.GroupService;
import com.github.noeeekr.servicerized.identity.services.user.UserService;
import com.github.noeeekr.servicerized.identity.services.user.request.CreateUserInterface;
import com.github.noeeekr.servicerized.identity.services.user.request.UserSignInInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserService userService;

    @Autowired
    private final GroupService groupService;

    @PersistenceContext
    private final EntityManager entityManager;

    public Response<User> createUserAccount(CreateUserInterface request) {
        return this.createUserAccount(request, new ServiceConfiguration());
    }

    public Response<User> createUserAccount(CreateUserInterface request, ServiceConfiguration configuration) {
        // Section: Create User & Handle Operation Errors
        Response<User> createUserResponse =
                userService.createUser(UserDto.fromCreateRequest(request), configuration);

        // Section: Handle Previous Section Errors
        if (createUserResponse.isSuccess() == false)
            return createUserResponse;

        // Section: Create User Access Group
        Group initialAccessGroup = GroupDto.createPrimaryAccessGroup(entityManager,
                createUserResponse.getPayload(), request.getPassword());

        Response<Group> createGroupResponse =
                groupService.createAccessGroup(initialAccessGroup, configuration);

        // Section: Handle Previous Section Errors
        if (createGroupResponse.isSuccess() == false) {
            return Response.<User>builder().fail(createGroupResponse.getFailure()).build();
        }

        return createUserResponse;
    }

    public Response<?> authorizeUserAccount(UUID emailConfirmationToken) {
        Response<?> response = userService.validateEmail(emailConfirmationToken);
        return response;
    }

    public Response<?> signUserAccount(UserSignInInterface request) {
        Response<?> response = userService.getBySignInCredentials(request);
        return response;
    }
}
