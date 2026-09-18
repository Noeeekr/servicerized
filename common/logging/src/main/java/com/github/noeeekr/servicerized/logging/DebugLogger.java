package com.github.noeeekr.servicerized.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.noeeekr.servicerized.response.failure.Failure;
import com.github.noeeekr.servicerized.response.failure.Failures;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugLogger {
    private static ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void displayEntity(String message, Object entity, String... domains) {
        String formattedDomains =
                DebugLogger.formatDomain(DebugLogger.class.getName(), DebugLogger.formatDomain(domains));
        message = String.format("%s: ", message);

        try {
            String json = objectMapper.writeValueAsString(entity);
            log.debug(String.format("%s%s\n\tEntity:\n%s", message, formattedDomains, json));
        } catch (Exception e) {
            DebugLogger.printStackTrace(new Failures.UnhandledException(e), domains);
            log.debug(String.format("%sUnable to display entity '%s'\n %s", formattedDomains,
                    entity.getClass().getName(), e.getMessage()));
        }
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
            for (int i = 1; i < domains.length; i++) {
                builder.append("\n\tat ");
                builder.append(domains[i]);
            }
            builder.append(": ");
            formattedDomain = builder.toString();
        }
        return formattedDomain;
    }
}
