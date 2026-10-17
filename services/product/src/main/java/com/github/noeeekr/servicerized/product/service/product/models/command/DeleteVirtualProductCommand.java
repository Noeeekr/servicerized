package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;

public class DeleteVirtualProductCommand implements ProductFieldsInterface.OwnerId {
    private UUID productOwnerId;

    private ProductFilter productFilter;

    public DeleteVirtualProductCommand(UUID productOwnerId, ProductFilter filter) {
        this.productFilter = filter;
        this.productOwnerId = productOwnerId;
    }

    public ProductFilter getProductFilter() {
        return this.productFilter;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }
}
