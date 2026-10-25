package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFilterableFieldsInterface;
import lombok.Builder;

@Builder
public class ProductInventoryFilter implements ProductInventoryFilterableFieldsInterface {
    private UUID productId;

    public ProductInventoryFilter(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public boolean isEmpty() {
        return this.productId == null;
    }
}
