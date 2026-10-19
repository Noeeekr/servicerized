package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.models.request.UpdateVirtualProductInformationRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.update.UpdateProductFields;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.update.UpdateVirtualProductFields;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;

public class UpdateVirtualProductInformationCommand
        implements UpdateVirtualProductInformationCommandInterface {
    private UUID productOwnerId;

    private ProductFilter productFilter;
    private UpdateProductFields productFields;
    private UpdateVirtualProductFields virtualProductFields;

    public UpdateVirtualProductInformationCommand(UUID productOwnerId,
            UpdateProductFieldsInterface productFields,
            UpdateVirtualProductFieldsInterface virtualProductFields,
            ProductFilterableFieldsInterface productFilter) {
        this.productFilter = new ProductFilter(productFilter);

        this.productFields = new UpdateProductFields(productFields);
        this.virtualProductFields = new UpdateVirtualProductFields(virtualProductFields);

        this.productOwnerId = productOwnerId;
    }

    public UUID getProductOwnerId() {
        return this.productOwnerId;
    }

    public ProductFilter getProductFilter() {
        return this.productFilter;
    }

    public UpdateProductFields getProduct() {
        return this.productFields;
    }

    public UpdateVirtualProductFieldsInterface getVirtualProduct() {
        return this.virtualProductFields;
    }

    public static UpdateVirtualProductInformationCommand upgrade(UUID productOwnerId,
            UpdateVirtualProductInformationRequestInterface request) {

        return new UpdateVirtualProductInformationCommand(productOwnerId, request.getProduct(),
                request.getVirtualProduct(), request.getProductFilter());
    }
}
