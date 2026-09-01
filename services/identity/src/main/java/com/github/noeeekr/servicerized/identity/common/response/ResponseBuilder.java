package com.github.noeeekr.servicerized.identity.common.response;

import javax.lang.model.type.ErrorType;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;

/**
 * ResponseBuilder facilitates the creation of class Response.
 * 
 * It does so by holding the value of the paramters passed by its setters and creating a response
 * with them at the end.
 * 
 * @param <Payload> the type of the payload response will carry on success
 * @param <ErrorType> the type of the error response will carry on failure
 */
public class ResponseBuilder<Payload> {
    private boolean success = true;
    private Payload payload = null;
    private Failure err = null;

    public Response<Payload> build() {
        return new Response<>(success, payload, err);
    }

    public ResponseBuilder<Payload> success(Payload payload) {
        this.payload = payload;
        return this;
    }

    public ResponseBuilder<Payload> success() {
        return this.success(null);
    }

    public ResponseBuilder<Payload> fail(Failure err) {
        this.success = false;
        this.err = err;
        return this;
    }
}
