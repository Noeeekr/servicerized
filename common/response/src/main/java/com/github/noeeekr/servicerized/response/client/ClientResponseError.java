package com.github.noeeekr.servicerized.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClientResponseError {
    private String description;
    private Boolean clientError;
}
