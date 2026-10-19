package com.github.noeeekr.servicerized.product.controller.models.request;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;

public interface UpdateProductRequestInterface {
        public ProductFilterableFieldsInterface getProductFilter();

        public UpdateProductFieldsInterface getProduct();
}
