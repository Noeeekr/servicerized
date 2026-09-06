package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.Dto;
import com.github.noeeekr.servicerized.identity.repository.models.User;

public record UserDto(UUID id, String name, String email) implements Dto {
    public static UserDto New(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
