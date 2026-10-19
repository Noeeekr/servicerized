package com.github.noeeekr.servicerized.product.service.product.models.command;

import com.github.noeeekr.servicerized.product.controller.models.request.UpdateVirtualProductInformationRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFieldsInterface;

/**
 * UpdateVirtualProductInformationCommandInterface defines the interface for a update operation of a
 * virtual product entity and the entities it extends. Which is ProductEntity.
 */
public interface UpdateVirtualProductInformationCommandInterface
        extends ProductFieldsInterface.OwnerId, UpdateVirtualProductInformationRequestInterface {
}
