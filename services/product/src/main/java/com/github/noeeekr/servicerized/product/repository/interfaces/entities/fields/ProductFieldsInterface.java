package com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields;

import java.util.UUID;

public interface ProductFieldsInterface {
    public interface Name {
        public String getProductName();
    }
    public interface Description {
        public String getProductDescription();
    }
    public interface Price {
        public Integer getProductPrice();
    }
    public interface OwnerId {
        public UUID getProductOwnerId();
    }
    public interface Id {
        public UUID getProductId();
    }
}
