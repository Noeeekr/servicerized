package com.github.noeeekr.servicerized.product.repository.interfaces.fields;

import java.util.UUID;

public interface CategoryFieldsInterface {
    public interface Id {
        public UUID getCategoryId();
    }
    public interface Name {
        public String getCategoryName();
    }
}

