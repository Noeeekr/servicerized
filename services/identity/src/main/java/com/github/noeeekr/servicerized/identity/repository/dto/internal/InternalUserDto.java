package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.SerializableDto;
import com.github.noeeekr.servicerized.identity.repository.interfaces.UserInterface;

public record InternalUserDto(UserInterface user) implements SerializableDto, UserInterface {
    @Override
    public String getUserEmail() {
        return user.getUserEmail();
    }

    @Override
    public UUID getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUserName() {
        return user.getUserName();
    }
}
