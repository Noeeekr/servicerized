package com.github.noeeekr.servicerized.product.repository.interfaces.operations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

/**
 * UpdateProductInterface defines a interface for a update operation that targets only the
 * ProductEntity.
 */
public interface UpdateProductFieldsInterface extends ProductFieldsInterface.Name,
        ProductFieldsInterface.Description, ProductFieldsInterface.Price {
}
