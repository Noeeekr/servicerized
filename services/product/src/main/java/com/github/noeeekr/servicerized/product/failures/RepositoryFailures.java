package com.github.noeeekr.servicerized.product.failures;

import org.springframework.http.HttpStatus;
import com.github.noeeekr.servicerized.response.failure.Failure;

public class RepositoryFailures {

    public record ResourceNotFound(String message) implements Failure {
        public String message() {
            return "Recurso não encontrado: " + message;
        }

        public HttpStatus code() {
            return HttpStatus.NOT_FOUND;
        }

        @Override
        public boolean isClientFault() {
            return true;
        }
    }

}
