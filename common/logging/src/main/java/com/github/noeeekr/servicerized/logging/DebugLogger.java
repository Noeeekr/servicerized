package com.github.noeeekr.servicerized.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.noeeekr.servicerized.response.failure.Failure;
import com.github.noeeekr.servicerized.response.failure.Failures;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugLogger {
    private static ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void displayEntity(String message, Object entity, String... domains) {
        String formattedDomains = DebugLogger.formatDomain(DebugLogger.class.getName(),
                DebugLogger.formatDomain(domains));
        message = String.format("Description: %s ", message);

        try {
            String json = objectMapper.writeValueAsString(entity);
            log.debug(String.format("%s\n\t%s\n\tEntity:\n%s", message, formattedDomains, json));
        } catch (Exception e) {
            DebugLogger.printStackTrace(new Failures.UnhandledException(e), domains);
            log.debug(String.format("%sUnable to display entity '%s'\n %s", formattedDomains,
                    entity.getClass().getName(), e.getMessage()));
        }
    }

    public static void displayFailure(Failure failure, String... domains) {
        if (failure.error() != null) {
            DebugLogger.displayThrowable(failure.error(), domains);
            return;
        }

        String.format("%s\n\tStatus Code: %s\n\tClient Fault: %b", failure.message(),
                failure.code().getReasonPhrase(), failure.isClientFault());
        DebugLogger.displayEntity(failure.message(), failure, domains);
    }

    public static void displayThrowable(Throwable ex, String... domains) {
        String formattedDomains = DebugLogger.formatDomain(DebugLogger.class.getName(),
                DebugLogger.formatDomain(domains));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        try (outputStream; printStream) {
            ex.printStackTrace(printStream);
        } catch (IOException ioE) {
            log.debug("Failed to print stack trace of an exception in domain: " + formattedDomains);
        }

        log.debug(String.format("%s\n%s", formattedDomains,
                outputStream.toString(StandardCharsets.UTF_8)));
    }

    public static void printStackTrace(Failure failure, String[] domains) {
        log.error(DebugLogger.formatDomain(domains) + failure.message());
        if (failure.error() != null)
            failure.error().printStackTrace();
    }

    public static void printStackTrace(Failure failure, String domains) {
        log.error(domains + failure.message());
        if (failure.error() != null)
            failure.error().printStackTrace();
    }

    public static String formatDomain(String... domains) {
        String formattedDomain;
        {
            StringBuilder builder = new StringBuilder();
            if (domains.length >= 1) {
                builder.append(domains[0]);
            }
            String message;
            for (int i = 1; i < domains.length; i++) {
                message = domains[i];
                builder.append("\n\tat ");
                builder.append(message);
            }
            builder.append(": ");
            formattedDomain = builder.toString();
        }
        return formattedDomain;
    }
}
