package com.github.noeeekr.servicerized.product.controller.models.request;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.update.UpdateVirtualProductFields;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import lombok.experimental.SuperBuilder;

/**
 * UpdateVirtualProductInformationRequest defines the default implementation of
 * UpdateVirtualProductInformationInterface.
 */
@SuperBuilder
public class UpdateVirtualProductInformationRequest extends UpdateProductRequest
        implements UpdateVirtualProductInformationRequestInterface {

    private UpdateVirtualProductFields virtualProductFields;

    public UpdateVirtualProductInformationRequest(UpdateProductFieldsInterface productFields,
            UpdateVirtualProductFieldsInterface virtualProductFields,
            ProductFilterableFieldsInterface filter) {
        super(productFields, filter);
        this.virtualProductFields = new UpdateVirtualProductFields(virtualProductFields);
    }

    public UpdateVirtualProductFields getVirtualProduct() {
        return this.virtualProductFields;
    }
}
