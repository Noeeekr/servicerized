package com.github.noeeekr.servicerized.identity.controller.response;

import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;

public class ClientResponseErrorDto {
    public final static ClientResponseError fromFailure(Failure failure) {
        return new ClientResponseError(failure.getClientSafeMessage(), failure.isClientFault());
    }
}
