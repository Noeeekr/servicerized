package com.github.noeeekr.servicerized.product.controller.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({@JsonSubTypes.Type(value = CreateProductRequestInterface.class,
        name = "create-product-request"),})
public interface CreateProductRequestInterface {
    public String getName();

    public String getDescription();

    public Integer getPrice();
}
