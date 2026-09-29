package com.github.noeeekr.servicerized.product.controller.models.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFieldsInterface;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({@JsonSubTypes.Type(value = CreateProductRequestInterface.class,
        name = "create-product-request"),})
public interface CreateVirtualProductRequestInterface
        extends CreateProductRequestInterface, VirtualProductFieldsInterface.ProvisionHours {
}
