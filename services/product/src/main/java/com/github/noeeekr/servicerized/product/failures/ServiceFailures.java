package com.github.noeeekr.servicerized.product.failures;

import org.springframework.http.HttpStatus;
import com.github.noeeekr.servicerized.response.failure.Failure;
import com.github.noeeekr.servicerized.response.failure.Failures;

public class ServiceFailures extends Failures {
    public record AuthorizationFailed(String message, boolean clientFault) implements Failure {
        public String message() {
            return "Falha de autorização: " + message;
        }

        public HttpStatus code() {
            return HttpStatus.UNAUTHORIZED;
        }

        @Override
        public boolean isClientFault() {
            return clientFault;
        }
    }

    public record BadRequest(String message) implements Failure {
        public String message() {
            return "incapaz de completar a requisição: " + message;
        }

        public HttpStatus code() {
            return HttpStatus.BAD_REQUEST;
        }

        @Override
        public boolean isClientFault() {
            return true;
        }
    }
}
