package com.github.noeeekr.servicerized.product.repository.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.ProductInterface;
import lombok.experimental.SuperBuilder;

@SuperBuilder 
public class ProductDto implements ProductInterface {
    private UUID id;
    private UUID ownerId;
    private String name;
    private Integer price;
    private String description;

    public ProductDto(UUID id, UUID ownerId, String name, Integer price, String description) {
        if (id.version() != 7) {
            throw new IllegalArgumentException(
                    String.format("Create dto for entity 'product': Required UUID version on field 'id' to be 'v7' found 'v%d'.", id.version()));
        }
        if (ownerId.version() != 7) {
            throw new IllegalArgumentException(
                    String.format("Create dto for entity 'product': Required UUID version on field 'ownerId' to be 'v7' found 'v%d'.", ownerId.version()));
        }
        this.description = description;
        this.ownerId = ownerId;
        this.price = price;
        this.name = name;
        this.id = id;
    }

    // Getters

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

    // Transformers

    public static ProductDto from(ProductInterface product) {
        return new ProductDto(product.getId(), product.getOwnerId(), product.getName(),
                product.getPrice(), product.getDescription());
    }
}
