package com.github.noeeekr.servicerized.identity.common.response.failure;

/**
 * Failures implements generic Failure types that are not related to any specific entity.
 */
public class Failures {
    public record UnhandledException(Exception e) implements Failure {
        @Override
        public Exception error() {
            return e;
        }

        @Override
        public String message() {
            return e.getMessage();
        }

        public String getClientSafeMessage() {
            String message;
            if (this.isClientFault()) {
                message = this.message();
            } else {
                message = "Erro Interno. ";
            }
            return message;
        }
    }
}
