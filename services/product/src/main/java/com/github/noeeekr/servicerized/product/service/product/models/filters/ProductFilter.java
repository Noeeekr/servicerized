package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.ProductFilterableFieldsInterface;

public class ProductFilter implements ProductFilterableFieldsInterface {
    private UUID productId;
    private String productName;

    public ProductFilter(ProductFilterableFieldsInterface product) {
        this.productId = product.getId();
        this.productName = product.getName();
    }

    public ProductFilter(UUID productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public UUID getId() {
        return this.productId;
    }

    public String getName() {
        return this.productName;
    }
}
