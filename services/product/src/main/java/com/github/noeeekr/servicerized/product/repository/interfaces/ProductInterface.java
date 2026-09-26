package com.github.noeeekr.servicerized.product.repository.interfaces;

import com.github.noeeekr.servicerized.product.repository.interfaces.fields.ProductFieldsInterface;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateProductCommandInterface;

public interface ProductInterface extends CreateProductCommandInterface, ProductFieldsInterface.Id {
}
