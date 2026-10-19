package com.github.noeeekr.servicerized.product.controller.models.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({@JsonSubTypes.Type(value = UpdateVirtualProductInformationRequest.class,
                name = "update-virtual-product-request")})
public interface UpdateVirtualProductInformationRequestInterface
                extends UpdateProductRequestInterface {
        public UpdateVirtualProductFieldsInterface getVirtualProduct();
}
