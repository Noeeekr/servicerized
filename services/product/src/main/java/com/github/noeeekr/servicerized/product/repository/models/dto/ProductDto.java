package com.github.noeeekr.servicerized.product.repository.models.dto;

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

    public ProductDto(ProductInterface product) {
        this(product.getProductId(), product.getProductOwnerId(), product.getProductName(),
                product.getProductPrice(), product.getProductDescription());
    }

    public ProductDto(UUID id, UUID ownerId, String name, Integer price, String description) {
        if (id.version() != 7) {
            throw new IllegalArgumentException(String.format(
                    "Create dto for entity 'product': Required UUID version on field 'id' to be 'v7' found 'v%d'.",
                    id.version()));
        }
        if (ownerId.version() != 7) {
            throw new IllegalArgumentException(String.format(
                    "Create dto for entity 'product': Required UUID version on field 'ownerId' to be 'v7' found 'v%d'.",
                    ownerId.version()));
        }
        this.description = description;
        this.ownerId = ownerId;
        this.price = price;
        this.name = name;
        this.id = id;
    }

    // GetProductters

    public UUID getProductId() {
        return this.id;
    }

    public UUID getProductOwnerId() {
        return this.ownerId;
    }

    public String getProductName() {
        return this.name;
    }

    public Integer getProductPrice() {
        return this.price;
    }

    public String getProductDescription() {
        return this.description;
    }

    // Transformers

    public static ProductDto from(ProductInterface product) {
        return new ProductDto(product.getProductId(), product.getProductOwnerId(),
                product.getProductName(), product.getProductPrice(),
                product.getProductDescription());
    }
}
