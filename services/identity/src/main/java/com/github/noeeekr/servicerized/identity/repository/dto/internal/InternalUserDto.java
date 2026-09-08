package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.Dto;
import com.github.noeeekr.servicerized.identity.repository.models.User;

public record InternalUserDto(UUID id, String name, String email) implements Dto {
    public static InternalUserDto New(User user) {
        return new InternalUserDto(user.getId(), user.getName(), user.getEmail());
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }
}
