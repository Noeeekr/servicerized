package com.github.noeeekr.servicerized.product.repository.interfaces.fields;

import java.util.UUID;

public interface ProductFieldsInterface {
    public interface Name {
        public String getName();
    }
    public interface Description {
        public String getDescription();
    }
    public interface Price {
        public Integer getPrice();
    }
    public interface OwnerId {
        public UUID getOwnerId();
    }
    public interface Id {
        public UUID getId();
    }
}
