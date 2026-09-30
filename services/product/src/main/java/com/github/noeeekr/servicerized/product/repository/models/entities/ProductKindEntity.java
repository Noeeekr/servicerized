package com.github.noeeekr.servicerized.product.repository.models.entities;

import java.util.UUID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * 
 * ProductKindEntity is meant to store the system values for the available product kinds. Business
 * service logic may vary based on the kind of the product.
 * 
 * <br/>
 * <br/>
 * 
 * Available Kinds:
 * 
 * <br/>
 * 
 * - virtual-service (virtual service)
 */

@Entity
@Table(name = ProductKindEntity.METADATA.TABLE_NAME)
public class ProductKindEntity {
    public static class METADATA {
        public static final String TABLE_NAME = "products_kinds";
    }

    //
    // Constructors
    //
    public ProductKindEntity(UUID productId, Long productKindId) {
        this.id = new ProductKindId(productId, productKindId);
    }

    @EmbeddedId
    private ProductKindId id;

    @OneToOne
    @MapsId(ProductKindId.METADATA.COLUMN_NAME_PRODUCT_ID)
    @JoinColumn(name = ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_kind_target_product_id"))
    private ProductEntity product;

    @OneToOne
    @MapsId(ProductKindId.METADATA.COLUMN_NAME_PRODUCT_KIND_ID)
    @JoinColumn(name = KindEntity.METADATA.COLUMN_NAME_KIND_ID,
            foreignKey = @ForeignKey(name = "fk_product_kind_id"))
    private KindEntity kind;
}


