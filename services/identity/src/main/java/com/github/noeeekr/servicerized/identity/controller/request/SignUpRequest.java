package com.github.noeeekr.servicerized.identity.controller.request;

import com.github.noeeekr.servicerized.identity.services.request.CreateUserRequest;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SignUpRequest extends CreateUserRequest {
}
