package com.github.noeeekr.servicerized.controller;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.github.noeeekr.servicerized.response.failure.Failure;
import com.github.noeeekr.servicerized.response.client.ClientResponse;
import com.github.noeeekr.servicerized.response.client.ClientResponseError;
import com.github.noeeekr.servicerized.response.client.ClientResponseErrorDto;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Controller {
    /**
     * Handles creating the client-safe error response.
     * 
     * @param failure The original failure that caused the problem.
     * @param domains The locations of the failure beginning from the most inner layer.
     */
    protected ResponseEntity<ClientResponse> handleFailure(Failure failure,
            String... domains) {

        DebugLogger.printStackTrace(failure, domains);

        ClientResponseError clientResponseError = ClientResponseErrorDto.fromFailure(failure);
        ClientResponse clientResponse = new ClientResponse(clientResponseError);
        HttpStatus statusCode = Objects.requireNonNull(failure.code());

        return new ResponseEntity<>(clientResponse, statusCode);
    }

}
