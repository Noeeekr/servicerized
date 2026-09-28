package com.github.noeeekr.servicerized.response.client;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import lombok.Getter;


/**
 * Response defines the structure of a default client JSON response header.
 * 
 * The header contains a payload field, which contains the actual data the response sends. The
 * header contains a error fiedl, which contains any errors that may happen while processing the
 * request.
 * 
 * This object is expected to be used only when sending client (in a structure way), for this
 * reason, it is poorly typed.
 * 
 * @param payload The data to send to client.
 * @param err The error to send to client.
 */
@Getter
public class ClientResponse {
    private ClientResponseError error;
    private Object payload;
    private Date createdAt;

    public ClientResponse(ClientResponseDto payload) {
        this.createdAt = new Date();
        this.payload = payload.prepareToClient();
        this.error = null;
    }

    public ClientResponse(Dictionary<?, ?> payload) {
        this.createdAt = new Date();
        this.payload = payload;
        this.error = null;
    }

    public ClientResponse(List<?> payload) {
        this.createdAt = new Date();
        this.payload = payload;
        this.error = null;
    }

    public ClientResponse(ClientResponseError err) {
        this.createdAt = new Date();
        this.payload = null;
        this.error = err;
    }

    public ClientResponse(Object payload, ClientResponseError err) {
        this.createdAt = new Date();
        this.payload = payload;
        this.error = err;
    }

    public static ClientResponseDto getEmptyPayload() {
        return new ClientResponseDto() {
            @Override
            public Object prepareToClient() {
                return null;
            }
        };
    }
}
