package com.github.noeeekr.servicerized.product.failures;

import org.springframework.http.HttpStatus;
import com.github.noeeekr.servicerized.response.failure.Failure;

public class Failures {
    public record AuthorizationFailed(String message, boolean clientFault) implements Failure {
        public String message() {
            return "Falha de autorização: " + message;
        }

        public HttpStatus code() {
            return HttpStatus.UNAUTHORIZED;
        }

        @Override 
        public boolean isClientFault() {
            return true;
        }
    }
}
