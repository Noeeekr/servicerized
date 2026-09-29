package com.github.noeeekr.servicerized.product.repository.interfaces.operations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;

public interface CreateVirtualProductInterface
        extends CreateProductInterface, VirtualProductFieldsInterface.ProvisionHours {

}
