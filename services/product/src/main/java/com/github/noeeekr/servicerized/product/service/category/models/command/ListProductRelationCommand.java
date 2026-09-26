package com.github.noeeekr.servicerized.product.service.category.models.command;

import java.util.UUID;

public record ListProductRelationCommand(UUID productId, UUID productOwnerId) {
    public UUID getProductId() {
        return this.productId;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }
}
