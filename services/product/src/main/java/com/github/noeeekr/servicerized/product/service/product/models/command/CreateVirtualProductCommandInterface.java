package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateVirtualProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.dto.VirtualProductDto;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

public interface CreateVirtualProductCommandInterface
        extends CreateVirtualProductRequestInterface, ProductFieldsInterface.OwnerId {
    //
    // Transformators
    //
    public static CreateVirtualProductCommandInterface upgrade(
            CreateVirtualProductRequestInterface request, UUID ownerId) {
        return new VirtualProductDto(null, ownerId, request.getName(), request.getPrice(),
                request.getDescription(), request.getProvisionHours());
    }
}
