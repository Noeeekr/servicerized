package com.github.noeeekr.servicerized.identity.repository.dto;

import com.github.noeeekr.servicerized.identity.controller.response.ClientResponseDto;
import com.github.noeeekr.servicerized.identity.repository.dto.client.ClientUserDto;
import com.github.noeeekr.servicerized.identity.repository.dto.internal.InternalUserDto;
import com.github.noeeekr.servicerized.identity.repository.models.User;

public class UserDto implements Dto, ClientResponseDto {
    private User user;

    public UserDto(User user) {
        this.user = user;
    }

    /* Static methods */

    public static InternalUserDto getInternalDto(User user) {
        return InternalUserDto.New(user);
    }

    public static ClientUserDto getClientDto(User user) {
        return ClientUserDto.New(user);
    }

    /* Instance methods */

    public InternalUserDto getInternalDto() {
        return InternalUserDto.New(this.user);
    }

    public ClientUserDto getClientDto() {
        return ClientUserDto.New(this.user);
    }

    /* Interface implementation methods */

    public Object prepareToClient() {
        return this.getClientDto();
    }
}
