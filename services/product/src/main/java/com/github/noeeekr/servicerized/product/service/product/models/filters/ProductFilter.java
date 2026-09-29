package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;

public class ProductFilter implements ProductFilterableFieldsInterface {
    private UUID productId;
    private String productName;

    public ProductFilter(ProductFilterableFieldsInterface product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
    }

    public ProductFilter(UUID productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public String getProductName() {
        return this.productName;
    }
}
