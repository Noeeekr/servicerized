package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

import java.util.UUID;

public interface ProductInventoryFieldsInterface {
    public interface AvailableAmount {
        public UUID getProductAvailableAmount();
    }
    public interface Id {
        public UUID getProductId();
    }
}
