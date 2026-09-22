package com.github.noeeekr.servicerized.identity.repository.dto.client;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.interfaces.UserInterface;

public record ClientUserDto(UserInterface user) implements UserInterface {
    @Override
    public String getUserEmail() {
        return this.user.getUserEmail();
    }

    @Override
    public String getUserName() {
        return this.user.getUserName();

    }

    @Override
    public UUID getUserId() {
        return this.user.getUserId();
    }
}
