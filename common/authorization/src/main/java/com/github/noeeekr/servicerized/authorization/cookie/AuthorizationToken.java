package com.github.noeeekr.servicerized.authorization.cookie;

import java.util.UUID;

public record AuthorizationToken(UUID userId, String version) {
};
