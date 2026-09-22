package com.github.noeeekr.servicerized.identity.repository.dto.internal;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.SerializableDto;
import com.github.noeeekr.servicerized.identity.repository.models.UserEmailConfirmation;

public record InternalUserEmailConfirmationDto(UUID user_id, UUID token, boolean confirmed)
        implements SerializableDto {
    public static InternalUserEmailConfirmationDto New(UserEmailConfirmation confirmation) {
        return new InternalUserEmailConfirmationDto(confirmation.getUser().getUserId(),
                confirmation.getToken(), confirmation.isConfirmed());
    }
}
