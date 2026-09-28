package com.github.noeeekr.servicerized.product.repository.models;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ProductKindRelationId implements Serializable {
    public static class METADATA {
        //
        // Refers to the column name of the class not the database
        //
        public static final String COLUMN_NAME_PRODUCT_ID = "productId";
        public static final String COLUMN_NAME_PRODUCT_KIND_ID = "productKindId";
    }

    //
    // Fields
    //

    @Column(name = "product_id")
    private final UUID productId;
    @Column(name = "product_kind_id")
    private final Long productKindId;

    //
    // Constructors
    //

    public ProductKindRelationId() {
        this.productId = null;
        this.productKindId = null;
    }

    public ProductKindRelationId(UUID productId, Long productKindId) {
        this.productId = productId;
        this.productKindId = productKindId;
    }

    //
    // Required methods (hibernate)
    //

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (o.getClass() != this.getClass())
            return false;

        ProductKindRelationId t = (ProductKindRelationId) o;
        if (t.getProductId() != this.getProductId())
            return false;
        if (t.getProductKindId() != this.getProductKindId())
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
