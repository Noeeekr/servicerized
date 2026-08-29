package com.github.noeeekr.servicerized.identity.common.response.failure;

import java.util.List;
import org.springframework.http.HttpStatus;

public class UserFailure {
    public record FailedPersist(Exception e, String message) implements Failure {
        public FailedPersist(String message) {
            this(null, message);
        }

        public FailedPersist(Exception e) {
            this(e, "Failed to persist data. ");
        }

        @Override
        public Exception error() {
            return e;
        }

        @Override
        public String message() {
            String message = this.message;
            if (e != null) {
                message += String.format(" %s.", e.getMessage());
            }
            return message;
        }
    }

    public record ResourceFound(String location, String resource) implements Failure {
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
            return String.format("resource %s on %s already exists.", resource, location);
        }
    }

    public record Duplicate(String resourceName, List<String> ids) implements Failure {
        @Override
        public String message() {
            String resourceIds = new String();
            ids.forEach((id) -> {
                resourceIds.concat(String.format("'%s', ", id));
            });

            return String.format(
                    "Duplicates were found in resource '%s' when searching for ids: %s ",
                    resourceName, resourceIds);
        }
    }
}
