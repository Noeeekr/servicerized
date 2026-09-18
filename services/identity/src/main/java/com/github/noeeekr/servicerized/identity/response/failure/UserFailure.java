package com.github.noeeekr.servicerized.identity.response.failure;

import java.util.List;
import org.springframework.http.HttpStatus;
import com.github.noeeekr.servicerized.response.failure.Failure;
import lombok.Builder;

public class UserFailure {
    @Builder
    public record EmailConfirmationPending(String message) implements Failure {
        @Override
        public String message() {
            return message;
        }

        @Override
        public HttpStatus code() {
            return HttpStatus.PRECONDITION_REQUIRED;
        }

        @Override
        public boolean isClientFault() {
            return true;
        }
    }

    @Builder
    public record FailedPersist(Exception error, String message) implements Failure {
        public FailedPersist(String message) {
            this(null, message);
        }

        public FailedPersist(Exception error) {
            this(error, "Failed to persist data. ");
        }

        @Override
        public Exception error() {
            return error;
        }

        @Override
        public String message() {
            String message = this.message;
            if (error != null) {
                message += String.format(" %s.", error.getMessage());
            }
            return message;
        }
    }

    @Builder
    public record ResourceFound(String message) implements Failure {
        @Override
        public HttpStatus code() {
            return HttpStatus.BAD_REQUEST;
        }

        @Override
        public boolean isClientFault() {
            return true;
        }

        @Override
        public String message() {
            return String.format(message);
        }
    }

    @Builder
    public record ResourceNotFound(String message) implements Failure {
        @Override
        public HttpStatus code() {
            return HttpStatus.BAD_REQUEST;
        }

        @Override
        public boolean isClientFault() {
            return true;
        }

        @Override
        public String message() {
            return String.format(message);
        }
    }

    @Builder
    public record Duplicate(String resource, List<String> duplicatedIds) implements Failure {

        @Override
        public String message() {
            String formattedDuplicateIds = new String();
            duplicatedIds.forEach((id) -> {
                formattedDuplicateIds.concat(String.format("'%s', ", id));
            });

            return String.format("Duplicates were found in resource '%s'. Duplicate Id's: %s ",
                    resource, formattedDuplicateIds);
        }
    }
}
