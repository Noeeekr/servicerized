package com.github.noeeekr.servicerized.identity.repository.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.identity.repository.dto.client.ClientUserDto;
import com.github.noeeekr.servicerized.identity.repository.dto.internal.InternalUserDto;
import com.github.noeeekr.servicerized.identity.repository.interfaces.UserInterface;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;

public class UserDto implements ClientResponseDto, UserInterface {
    private User user;

    public UserDto(User user) {
        this.user = user;
    }

    /* Static methods */

    public static InternalUserDto getInternalDto(UserInterface user) {
        return new InternalUserDto(user);
    }

    public static ClientUserDto getClientDto(User user) {
        return new ClientUserDto(user);
    }

    /* Instance methods */

    public UserInterface getInternalDto() {
        return new InternalUserDto(this);
    }

    public UserInterface getClientDto() {
        return new ClientUserDto(this.user);
    }

    /* Interface implementation methods */

    public Object prepareToClient() {
        return this.getClientDto();
    }

    /* Getters */
    public String getUserEmail() {
        return this.user.getUserEmail();
    }

    public String getUserName() {
        return this.user.getUserName();

    }

    public UUID getUserId() {
        return this.user.getUserId();

    }

    public String getPassword() {
        return "";
    }
}
