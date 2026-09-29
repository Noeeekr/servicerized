package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.CreateProductInterface;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

public interface CreateProductCommandInterface extends CreateProductInterface {
    @AllArgsConstructor
    public final class CreateProductCommandAdapter implements CreateProductCommandInterface {
        @Delegate
        private CreateProductInterface product;
    }

    //
    // Transformators
    //
    public static CreateProductCommandInterface upgrade(CreateProductRequestInterface request,
            UUID ownerId) {
        ProductDto dto = new ProductDto(null, ownerId, request.getProductName(),
                request.getProductPrice(), request.getProductDescription());
        return new CreateProductCommandAdapter(dto);
    }
}
