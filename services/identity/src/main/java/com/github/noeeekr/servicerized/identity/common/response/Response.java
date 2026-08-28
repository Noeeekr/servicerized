package com.github.noeeekr.servicerized.identity.common.response;

import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Response<PayloadType> {
    private boolean success = true;
    private PayloadType payload = null;
    private Failure err = null;

    public static <Payload> ResponseBuilder<Payload> newInstance() {
        return new ResponseBuilder<>();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public PayloadType getPayload() {
        return this.payload;
    }

    public Failure getFailure() {
        return this.err;
    }
}
