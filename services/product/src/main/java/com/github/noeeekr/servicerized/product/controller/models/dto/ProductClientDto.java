package com.github.noeeekr.servicerized.product.controller.models.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.dto.ProductDto;

/**
 * ProductClientDto extends ProductDto making it a class safe for client interaction.
 */
public class ProductClientDto extends ProductDto {
    public ProductClientDto(UUID id, UUID ownerId, String name, Integer price, String description) {
        super(id, ownerId, name, price, description);
    }
}
