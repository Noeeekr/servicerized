package com.github.noeeekr.servicerized.identity.common.configuration;

import org.springframework.beans.factory.annotation.Value;

public class ServerConfiguration {
    @Value("${app.domain}")
    private static String DOMAIN;

    public static String getDomain() {
        return ServerConfiguration.DOMAIN;
    }
}