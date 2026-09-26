package com.github.noeeekr.servicerized.response.client;

public interface ClientResponseDto {
    Object prepareToClient();

    final ClientResponseDto EmptyPayload = new ClientResponseDto() {
        @Override
        public Object prepareToClient() {
            return null;
        }
    };
}
