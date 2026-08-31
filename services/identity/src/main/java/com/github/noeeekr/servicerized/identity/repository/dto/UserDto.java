package com.github.noeeekr.servicerized.identity.repository.dto;

import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.user.request.CreateUserInterface;

public class UserDto {
    public static User prepareForClient(User user) {
        return user;
    }

    public static User fromCreateRequest(CreateUserInterface user) {
        return new User().setEmail(user.getEmail()).setName(user.getName());
    }
}
