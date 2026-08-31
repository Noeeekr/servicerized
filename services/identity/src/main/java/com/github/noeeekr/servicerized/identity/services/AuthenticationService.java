package com.github.noeeekr.servicerized.identity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.identity.common.response.Response;
import com.github.noeeekr.servicerized.identity.repository.dto.UserDto;
import com.github.noeeekr.servicerized.identity.repository.models.Group;
import com.github.noeeekr.servicerized.identity.repository.models.GroupKind;
import com.github.noeeekr.servicerized.identity.repository.models.GroupKinds;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.configuration.ServiceConfiguration;
import com.github.noeeekr.servicerized.identity.services.request.CreateUserInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationService {
    @Autowired
    private final UserService userService;
    @Autowired
    private final GroupService groupService;
    @PersistenceContext
    private final EntityManager entityManager;

    public Response<User> newUser(CreateUserInterface request) {
        return this.newUser(request, new ServiceConfiguration());
    }

    public Response<User> newUser(CreateUserInterface request, ServiceConfiguration configuration) {
        // Create User & Handle Operation Errors
        User user = UserDto.fromCreateRequest(request);
        Response<User> createUserResponse = userService.createUser(user, configuration);

        if (createUserResponse.isSuccess() == false) {
            return createUserResponse;
        }

        GroupKind groupKindReference =
                entityManager.getReference(GroupKind.class, GroupKinds.AccessGroup.getId());

        // Create User Access Group & Handle Operation Errors
        Group initialAccessGroup = new Group();
        initialAccessGroup.setPassword(request.getPassword());
        // Group Kind needs to come from pre-defined on database;
        initialAccessGroup.setGroupKindId(groupKindReference);
        initialAccessGroup.setOwnerId(user);

        Response<Group> createGroupResponse =
                groupService.createAccessGroup(initialAccessGroup, configuration);
        if (createGroupResponse.isSuccess() == false) {
            return Response.<User>builder().fail(createGroupResponse.getFailure()).build();
        }

        return createUserResponse;
    }
}
