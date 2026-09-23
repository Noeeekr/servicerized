package com.github.noeeekr.servicerized.product.repository.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ProductCategoryKey implements Serializable {
    public static final class METADATA {
        public static final String COLUMN_NAME_PRODUCT_ID = "productId";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
    }

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Override
    public boolean equals(Object target) {
        if (target == this)
            return true;
        if (target == null || getClass() != target.getClass())
            return false;

        ProductCategoryKey targetEntity = (ProductCategoryKey) target;
        return (this.getProductId() == targetEntity.getProductId())
                && (this.getCategoryId() == targetEntity.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getCategoryId(), this.getProductId());
    }
}
