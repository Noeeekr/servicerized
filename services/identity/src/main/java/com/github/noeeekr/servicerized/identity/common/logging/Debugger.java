package com.github.noeeekr.servicerized.identity.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failure;
import com.github.noeeekr.servicerized.identity.common.response.failure.Failures;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Debugger {
    public static void displayEntity(String message, Object entity, String... domains) {
        String formattedDomains =
                Debugger.formatDomain(Debugger.class.getName(), Debugger.formatDomain(domains));
        message = String.format("%s: ", message);

        try {
            String json = new ObjectMapper().writeValueAsString(entity);
            log.debug("%s%s\nEntity:\n%s", message, formattedDomains, json);
        } catch (Exception e) {
            Debugger.printStackTrace(new Failures.UnhandledException(e), domains);
            log.debug("%sUnable to display entity '%s'\n %s", formattedDomains,
                    entity.getClass().getName(), e.getMessage());
        }
    }

    public static void printStackTrace(Failure failure, String[] domains) {
        log.error(Debugger.formatDomain(domains) + failure.message());
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
                builder.append(": ");
                builder.append(domains[i]);
            }
            builder.append(": ");
            formattedDomain = builder.toString();
        }
        return formattedDomain;
    }
}
