package com.github.noeeekr.servicerized.identity.repository.dto.client;

import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.user.request.CreateUserInterface;

@Component
public class ClientUserDtoUtils {
    public static User prepareForClient(User user) {
        return user;
    }

    public static User fromCreateRequest(CreateUserInterface user) {
        return new User().setEmail(user.getEmail()).setName(user.getName());
    }
}
