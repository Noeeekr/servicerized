package com.github.noeeekr.servicerized.response.client;

import com.github.noeeekr.servicerized.response.failure.Failure;

public class ClientResponseErrorDto {
    public final static ClientResponseError fromFailure(Failure failure) {
        return new ClientResponseError(failure.getClientSafeMessage(), failure.isClientFault());
    }
}
