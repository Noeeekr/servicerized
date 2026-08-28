package com.github.noeeekr.servicerized.identity.common.response.failure;

public enum FailureCodes {
    UnhandledException(0), Duplicate(1), ResourceFound(2), FailedPersist(3);

    private final int code;

    FailureCodes(int code) {
        this.code = code;
    };

    public int getCode() {
        return this.code;
    }
}
