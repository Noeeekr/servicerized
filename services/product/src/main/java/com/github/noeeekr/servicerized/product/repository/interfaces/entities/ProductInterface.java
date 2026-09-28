package com.github.noeeekr.servicerized.product.repository.interfaces.entities;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateProductCommandInterface;

public interface ProductInterface extends CreateProductCommandInterface, ProductFieldsInterface.Id {
}
