package com.github.noeeekr.servicerized.product.service.request;

import java.util.UUID;

public record FindOneProductRequest(UUID productId, UUID productOwnerId) {
    public UUID getProductId() {
        return this.productId;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }
}
