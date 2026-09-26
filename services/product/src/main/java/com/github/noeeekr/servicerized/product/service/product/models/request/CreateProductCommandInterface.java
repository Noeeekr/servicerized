package com.github.noeeekr.servicerized.product.service.product.models.request;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.request.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.dto.ProductDto;

public interface CreateProductCommandInterface extends CreateProductRequestInterface {
    public UUID getOwnerId();

    public static CreateProductCommandInterface fromUpgrade(CreateProductRequestInterface request,
            UUID ownerId) {
        return new ProductDto(null, ownerId, request.getName(), request.getPrice(),
                request.getDescription());
    }
}
