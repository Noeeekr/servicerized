package com.github.noeeekr.servicerized.identity.controller.request;

import com.github.noeeekr.servicerized.identity.services.request.CreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SignUpRequest extends CreateUserRequest {
}
