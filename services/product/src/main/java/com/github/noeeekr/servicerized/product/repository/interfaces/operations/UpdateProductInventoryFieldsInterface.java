package com.github.noeeekr.servicerized.product.repository.interfaces.operations;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFieldsInterface;

public interface UpdateProductInventoryFieldsInterface
                extends ProductInventoryFieldsInterface.NullableAvailableAmount {
        public Integer getProductAvailableAmount();

}
