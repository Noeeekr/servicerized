package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFilterableFieldsInterface;

public class VirtualProductFilter implements VirtualProductFilterableFieldsInterface {
    private UUID productId;

    public VirtualProductFilter(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return this.productId;
    }
}
