package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.dto.ProductDto;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

public interface CreateProductCommandInterface
        extends CreateProductRequestInterface, ProductFieldsInterface.OwnerId {
    //
    // Transformators
    //
    public static CreateProductCommandInterface upgrade(CreateProductRequestInterface request,
            UUID ownerId) {
        return new ProductDto(null, ownerId, request.getName(), request.getPrice(),
                request.getDescription());
    }
}
