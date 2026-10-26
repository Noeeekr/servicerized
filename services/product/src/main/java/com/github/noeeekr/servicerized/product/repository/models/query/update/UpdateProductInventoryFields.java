package com.github.noeeekr.servicerized.product.repository.models.query.update;

import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductInventoryFieldsInterface;

public class UpdateProductInventoryFields implements UpdateProductInventoryFieldsInterface {
    private Integer availableAmount;

    public UpdateProductInventoryFields(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Integer getProductAvailableAmount() {
        return this.availableAmount;
    }
}
