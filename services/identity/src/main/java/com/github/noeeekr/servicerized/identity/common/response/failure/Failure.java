package com.github.noeeekr.servicerized.identity.common.response.failure;

import org.springframework.http.HttpStatus;

public interface Failure {
    default public HttpStatus code() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    String message();

    default public Exception error() {
        return null;
    }

    default public boolean isClientFault() {
        return false;
    }

    default public String getClientSafeMessage() {
        String message;
        if (this.isClientFault()) {
            message = this.message();
        } else {
            message = "Erro interno. ";
        }
        return message;
    }
}
