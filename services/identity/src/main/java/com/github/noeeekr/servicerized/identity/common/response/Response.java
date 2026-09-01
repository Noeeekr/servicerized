package com.github.noeeekr.servicerized.identity.common.response;

import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Response<PayloadType> {
    private boolean success;
    private PayloadType payload;
    private Failure failure;

    public static <Payload> ResponseBuilder<Payload> builder() {
        return new ResponseBuilder<>();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public PayloadType getPayload() {
        return this.payload;
    }

    public Response<PayloadType> fail(Failure failure) {
        this.failure = failure;
        this.success = false;
        return this;
    }

    public Failure getFailure() {
        return this.failure;
    }
}
