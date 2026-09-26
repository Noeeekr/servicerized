package com.github.noeeekr.servicerized.product.service.category.models.request;

import java.util.UUID;

public record ListProductRelationRequest(UUID productId, UUID productOwnerId) {
    public UUID getProductId() {
        return this.productId;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }
}
