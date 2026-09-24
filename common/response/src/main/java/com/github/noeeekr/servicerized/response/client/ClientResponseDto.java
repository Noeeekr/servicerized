package com.github.noeeekr.servicerized.response.client;

/**
 * ClientResponseDto is a transformer interface for client responses. Its intended purpose is to
 * abstract logic for object transformation for client responses.
 */
public interface ClientResponseDto {
    /**
     * prepareToClient() returns a version of the object that is serializable and client safe.
     * 
     * This method is expected to be used with JSON serialization, for this reason, it should
     * provide clear key-value implementations like classes instead of interfaces or records.
     * 
     * @return
     */
    Object prepareToClient();

    final ClientResponseDto EmptyPayload = new ClientResponseDto() {
        @Override
        public Object prepareToClient() {
            return null;
        }
    };
}
