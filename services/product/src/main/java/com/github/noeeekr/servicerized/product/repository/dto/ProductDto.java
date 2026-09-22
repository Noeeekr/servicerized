package com.github.noeeekr.servicerized.product.repository.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.ProductInterface;
import lombok.Builder;

@Builder
public record ProductDto(UUID id, UUID ownerId, String name, Integer price, String description)
        implements ProductInterface {
    public ProductDto {
        if (id.version() != 7 || ownerId.version() != 7) {
            throw new IllegalArgumentException(
                    "create dto 'product' failed: UUID version on field 'id' or 'ownerId' is different from 'v7'.");
        }
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
    }

    public Integer getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }
}
