package com.github.noeeekr.servicerized.product.controller.models.request;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.query.update.UpdateProductFields;
import com.github.noeeekr.servicerized.product.repository.models.query.update.UpdateVirtualProductFields;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * UpdateVirtualProductInformationRequest defines the default implementation of
 * UpdateVirtualProductInformationInterface.
 */
@Setter
@Getter 
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor 
public class UpdateVirtualProductInformationRequest extends UpdateProductRequest
        implements UpdateVirtualProductInformationRequestInterface {

    private UpdateVirtualProductFields virtualProductFields;

    public UpdateVirtualProductInformationRequest(UpdateProductFieldsInterface productFields,
            UpdateVirtualProductFieldsInterface virtualProductFields,
            ProductFilterableFieldsInterface filter) {
        super(productFields, filter);
        this.virtualProductFields = new UpdateVirtualProductFields(virtualProductFields);
    }

    public UpdateVirtualProductInformationRequest(UpdateProductFields productFields,
            UpdateVirtualProductFields virtualProductFields, ProductFilter filter) {
        super(productFields, filter);
        this.virtualProductFields = new UpdateVirtualProductFields(virtualProductFields);
    }

    public UpdateVirtualProductFields getVirtualProduct() {
        return this.virtualProductFields;
    }
}
