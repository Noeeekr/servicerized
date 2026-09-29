package com.github.noeeekr.servicerized.product.repository.models.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.VirtualProductInterface;
import lombok.Getter;

@Getter
public class VirtualProductDto implements VirtualProductInterface {
    private int provisionHours;
    private UUID productId;

    public VirtualProductDto(UUID productId, int provisionHours) {
        this.provisionHours = provisionHours;
        this.productId = productId;
    }

    public VirtualProductDto(VirtualProductInterface product) {
        this.provisionHours = product.getProvisionHours();
        this.productId = product.getProductId();
    }
}
