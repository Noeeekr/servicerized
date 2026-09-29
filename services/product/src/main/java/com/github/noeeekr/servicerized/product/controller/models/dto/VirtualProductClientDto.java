package com.github.noeeekr.servicerized.product.controller.models.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.models.dto.VirtualProductDto;

/**
 * VirtualProductClientDto extends VirtualProductDto making it a client safe class.
 */
public class VirtualProductClientDto extends VirtualProductDto {
    public VirtualProductClientDto(UUID id, UUID ownerId, String name, Integer price,
            String description, int provisionHours) {
        super(id, ownerId, name, price, description, provisionHours);
    }
}
