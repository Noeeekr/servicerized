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
public class CreateUserRequest implements CreateUserInterface {
    @Builder.Default
    private String name = "";
    @Builder.Default
    private String email = "";
    @Builder.Default
    private String password = "";
}
