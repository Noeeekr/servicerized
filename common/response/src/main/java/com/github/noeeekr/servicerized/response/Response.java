package com.github.noeeekr.servicerized.response;

import com.github.noeeekr.servicerized.response.failure.Failure;
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

    /**
     * success() creates a successfull response, containing the expected payload.
     * 
     * @param <T> The target type of the new response.
     * @param payload The expected payload.
     * @return A new successfull response.
     */
    public static <T> Response<T> success(T payload) {
        return Response.<T>builder().success(payload).build();
    }

    /**
     * fromFailure() creates a failed response. Extracts the failure of the original response.
     * 
     * @param <T> The target type of the new response.
     * @param originalResponse The original response to extract the failure from.
     * @return A new failed response with the same failure.
     */
    public static <T> Response<T> fromFailure(Response<?> originalResponse) {
        return Response.<T>builder().fail(originalResponse.getFailure()).build();
    }


    /**
     * fromFailure() creates a failed response directly from a failure.
     * 
     * @param <T> The target type of the new response.
     * @param failure The failure this response represents.
     * @return A new failed response.
     */
    public static <T> Response<T> fromFailure(Failure failure) {
        return Response.<T>builder().fail(failure).build();
    }
}
