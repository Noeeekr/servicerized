package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;

public class ProductFilter implements ProductFilterableFieldsInterface {
    private UUID productId;
    private UUID productOwnerId;

    private String productName;

    public ProductFilter(ProductFilterableFieldsInterface product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productOwnerId = product.getProductOwnerId();
    }

    public ProductFilter(UUID productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }

    public String getProductName() {
        return this.productName;
    }

    public boolean isEmpty() {
        return this.productId != null && this.productName != null && this.productOwnerId != null;
    }
}
