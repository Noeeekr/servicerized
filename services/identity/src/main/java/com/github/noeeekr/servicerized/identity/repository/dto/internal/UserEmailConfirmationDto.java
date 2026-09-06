package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.Dto;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;

public record UserEmailConfirmationDto(UUID user_id, UUID token, boolean confirmed) implements Dto {
    public static UserEmailConfirmationDto New(UserEmailConfirmation confirmation) {
        return new UserEmailConfirmationDto(confirmation.getUser().getId(), confirmation.getToken(),
                confirmation.isConfirmed());
    }
}
