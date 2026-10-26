package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

import java.util.UUID;

public interface ProductInventoryFieldsInterface {
    public interface AvailableAmount {
        public int getProductAvailableAmount();
    }

    /**
     * Nullable version of {@link AvailableAmount} since field can be null before database
     * insertion.
     */
    public interface NullableAvailableAmount {
        public Integer getProductAvailableAmount();
    }

    public interface Id {
        public UUID getProductId();
    }
}
