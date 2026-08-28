package com.github.noeeekr.servicerized.identity.common.response.failure;

public interface Failure {
    FailureCodes code();

    String message();

    default public Exception error() {
        return null;
    }

    default public boolean isInternal() {
        return true;
    }

    default public String getClientSafeMessage() {
        String message;
        if (this.isInternal()) {
            message = "Unknown error";
        } else {
            message = this.message();
        }
        return message;
    }
}
