package com.github.noeeekr.servicerized.product.repository.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

public class ProductKindEntity {
    @EmbeddedId
    private ProductKindId id;

    @OneToOne
    @MapsId(ProductKindId.METADATA.COLUMN_NAME_PRODUCT_ID)
    @JoinColumn(name = ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_product_id"))
    private ProductEntity product;

    @OneToOne
    @MapsId(ProductKindId.METADATA.COLUMN_NAME_PRODUCT_KIND_ID)
    @JoinColumn(name = KindEntity.METADATA.COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_product_kind_id"))
    private KindEntity productKind;
}
