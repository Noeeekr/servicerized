package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.Dto;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;

public record InternalUserEmailConfirmationDto(UUID user_id, UUID token, boolean confirmed) implements Dto {
    public static InternalUserEmailConfirmationDto New(UserEmailConfirmation confirmation) {
        return new InternalUserEmailConfirmationDto(confirmation.getUser().getId(), confirmation.getToken(),
                confirmation.isConfirmed());
    }
}
