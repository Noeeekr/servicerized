package com.github.noeeekr.servicerized.identity.repository.dto.client;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClientUserDto {
    private UUID clientId;
    private String clientName;
    private String clientEmail;

    public static ClientUserDto New(User user) {
        return new ClientUserDto(user.getId(), user.getName(), user.getEmail());
    }
}
