package com.github.noeeekr.servicerized.response;

import java.util.Objects;
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

    /**
     * (non-Javadoc)
     * 
     * equals(Object o) checks if the response (and its payload, failure, success etc) are
     * equivalent to another response object. For success response type only checks fields that are
     * expected on success and on failure type only checks fields that are expected on failure.
     * 
     * @implNote equals(Object o) depends on the payload to actually implement equals(Object o)
     *           validation correctly to be fully precise.
     * 
     * @param o A object to check if is a response.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Response<?> other))
            return false;
        if (this.success != other.success)
            return false;

        if (this.success) {
            return Objects.equals(this.payload, other.payload);
        }

        if (this.failure == null || other.failure == null) {
            return this.failure == other.failure;
        }

        return this.failure.getClass() == other.failure.getClass()
                && Objects.equals(this.failure.getClientSafeMessage(),
                        other.failure.getClientSafeMessage())
                && this.failure.code() == other.failure.code();
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
