package com.github.noeeekr.servicerized.response;

import com.github.noeeekr.servicerized.response.failure.Failure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (o.getClass() != this.getClass())
            return false;
        Response<?> response = (Response<?>) o;
        if (this.success == false) {
            return this.payloadEquals(response.getPayload());
        } else {
            return this.failureEquals(response.getFailure());
        }
    }

    /**
     * payloadEquals() checks if the response payload is the same as the target one.
     * 
     * @param p The target/expected payload.
     * @return a boolean that is true if both are equal.
     */
    private boolean payloadEquals(Object p) {
        if (this.payload.getClass() != p.getClass())
            return false;
        return this.payload.equals(p);
    }

    /**
     * failureEquals() checks if the response failure is the same as the target one.
     * 
     * @param f The target/expected failure.
     * @return a boolean that is true if both are equal.
     */
    public boolean failureEquals(Failure f) {
        if (this.failure == f)
            return true;
        if (this.failure.getClass() != f.getClass())
            return false;
        if (this.failure.getClientSafeMessage() != f.getClientSafeMessage())
            return false;
        if (this.failure.code() != f.code())
            return false;
        return true;
    }

    /**
     * success() creates a successfull response, containing the expected payload.
     * 
     * @param <T> The target type of the new response.
     * @param payload The expected payload.
     * @return A new successfull response.
     */
    public static <T> Response<T> success(T payload) {
        return Response.<T>builder().payload(payload).success(true).build();
    }

    /**
     * success() swtiches the success payload of the response with another response's payload.
     * 
     * @param payload Another response.
     * @return a reference for this response.
     */
    public Response<PayloadType> replacePayload(Response<PayloadType> payload) {
        this.payload = payload.getPayload();
        return this;
    }

    /**
     * success() swtiches the success payload of the response with another payload.
     * 
     * @param payload The expected payload.
     * @return a reference for this response.
     */
    public Response<PayloadType> replacePayload(PayloadType payload) {
        this.payload = payload;
        return this;
    }

    /**
     * fromFailure() creates a failed response. Extracts the failure of the original response.
     * 
     * @param <T> The target type of the new response.
     * @param originalResponse The original response to extract the failure from.
     * @return A new failed response with the same failure.
     */
    public static <T> Response<T> fromFailure(Response<?> originalResponse) {
        return Response.fromFailure(originalResponse.getFailure());
    }


    /**
     * fromFailure() creates a failed response directly from a failure.
     * 
     * @param <T> The target type of the new response.
     * @param failure The failure this response represents.
     * @return A new failed response.
     */
    public static <T> Response<T> fromFailure(Failure failure) {
        return Response.<T>builder().failure(failure).success(false).build();
    }
}
