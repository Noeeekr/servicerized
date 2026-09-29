package com.github.noeeekr.servicerized.product.repository.interfaces.entities;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.CreateProductInterface;

public interface ProductInterface extends CreateProductInterface, ProductFieldsInterface.Id {
}
