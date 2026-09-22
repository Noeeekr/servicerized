package com.github.noeeekr.servicerized.identity.services.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class CreateUserRequest implements CreateUserRequestInterface {
    @Builder.Default
    private String userName = "";
    @Builder.Default
    private String userEmail = "";
    @Builder.Default
    private String groupPassword = "";
}
