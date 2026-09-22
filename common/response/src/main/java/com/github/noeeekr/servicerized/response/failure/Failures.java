package com.github.noeeekr.servicerized.response.failure;

/**
 * Failures implements generic Failure types that are not related to any specific entity.
 */
public class Failures {
    public record NotImplemented(String procedureDescription, String origin) implements Failure {
        @Override
        public Exception error() {
            return new Exception(this.message());
        }

        @Override
        public String message() {
            return String.format("Unable to finish operation. The following procedure is not implemented: '%s' at origin '%s'", procedureDescription, origin);
        }
    }

    public record UnhandledException(Exception e) implements Failure {
        @Override
        public Exception error() {
            return e;
        }

        @Override
        public String message() {
            return e.getMessage();
        }
    }
}
