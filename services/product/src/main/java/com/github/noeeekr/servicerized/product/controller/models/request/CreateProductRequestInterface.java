package com.github.noeeekr.servicerized.product.controller.models.request;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

public interface CreateProductRequestInterface extends ProductFieldsInterface.Name,
        ProductFieldsInterface.Description, ProductFieldsInterface.Price {

}