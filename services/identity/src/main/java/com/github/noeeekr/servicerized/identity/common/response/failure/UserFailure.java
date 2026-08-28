package com.github.noeeekr.servicerized.identity.common.response.failure;

import java.util.List;

public class UserFailure {
    public record FailedPersist(Exception e) implements Failure {
        @Override
        public FailureCodes code() {
            return FailureCodes.FailedPersist;
        }

        @Override
        public Exception error() {
            return e;
        }

        @Override
        public String message() {
            return e.getMessage();
        }
    }

    public record UnhandledException(Exception e) implements Failure {
        @Override
        public FailureCodes code() {
            return FailureCodes.UnhandledException;
        }

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
            if (this.isInternal()) {
                message = "Unknown error";
            } else {
                message = this.message();
            }
            return message;
        }
    }

    public record ResourceFound(String location, String resource) implements Failure {
        @Override
        public FailureCodes code() {
            return FailureCodes.ResourceFound;
        }

        @Override
        public boolean isInternal() {
            return false;
        }

        @Override
        public String message() {
            return String.format("resource %s on %s already exists.", resource, location);
        }
    }

    public record Duplicate(String resourceName, List<String> ids) implements Failure {
        @Override
        public FailureCodes code() {
            return FailureCodes.Duplicate;
        }

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
