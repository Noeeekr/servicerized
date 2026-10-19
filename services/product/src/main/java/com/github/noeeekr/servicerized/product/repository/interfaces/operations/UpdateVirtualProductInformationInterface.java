package com.github.noeeekr.servicerized.product.repository.interfaces.operations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;

/**
 * UpdateVirtualProductInformationRequestInterface defines a interface for update operation on all
 * entities that contains cliente-customizable data targeting a virtual product. Which includes
 * entities like VirtualProductEntity and ProductEntity.
 */
public interface UpdateVirtualProductInformationInterface
        extends VirtualProductFieldsInterface.ProvisionHours, UpdateProductFieldsInterface {

}
