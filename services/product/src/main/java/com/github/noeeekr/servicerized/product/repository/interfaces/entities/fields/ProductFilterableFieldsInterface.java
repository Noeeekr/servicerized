package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

public interface ProductFilterableFieldsInterface extends ProductFieldsInterface.Name,
        ProductFieldsInterface.Id, ProductFieldsInterface.OwnerId {
    public boolean isEmpty();
}
