package com.github.noeeekr.servicerized.product.repository.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateVirtualProductCommandInterface;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VirtualProductDto extends ProductDto implements CreateVirtualProductCommandInterface {
    private int provisionHours;

    public VirtualProductDto(UUID id, UUID ownerId, String name, Integer price, String description,
            int provisionHours) {
        super(id, ownerId, name, price, description);
        this.provisionHours = provisionHours;
    }
}
