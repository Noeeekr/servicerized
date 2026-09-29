package com.github.noeeekr.servicerized.product.repository.interfaces.operations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

public interface CreateProductInterface
        extends ProductFieldsInterface.Name, ProductFieldsInterface.Description,
        ProductFieldsInterface.Price, ProductFieldsInterface.OwnerId {
}
