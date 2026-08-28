package com.github.noeeekr.servicerized.identity.repository.dto;

import com.github.noeeekr.servicerized.identity.controller.request.SignUpRequest;
import com.github.noeeekr.servicerized.identity.repository.models.User;

public class UserDto {
    public static User prepareForClient(User user) {
        user.setPassword("");
        return user;
    }

    public static User fromCreateRequest(SignUpRequest request) {
        return new User().setEmail(request.getEmail()).setPassword(request.getPassword())
                .setName(request.getName());
    }
}
