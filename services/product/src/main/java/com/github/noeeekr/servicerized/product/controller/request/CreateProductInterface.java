package com.github.noeeekr.servicerized.product.controller.request;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.dto.ProductDto;

public interface CreateProductInterface extends CreateProductRequestInterface {
    public UUID getOwnerId();

    public static CreateProductInterface fromUpgrade(CreateProductRequestInterface request,
            UUID ownerId) {
        return new ProductDto(null, ownerId, request.getName(), request.getPrice(),
                request.getDescription());
    }
}
