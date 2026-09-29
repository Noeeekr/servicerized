package com.github.noeeekr.servicerized.product.repository.models.entities;

import com.github.noeeekr.servicerized.product.repository.dto.ProductCategoryRelationDto;
import com.github.noeeekr.servicerized.repository.models.MetricsEntity;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = ProductCategoryEntity.METADATA.TABLE_NAME, schema = Models.SCHEMA)
public class ProductCategoryEntity extends MetricsEntity implements ClientResponseDto {
    public static final class METADATA {
        public static final String TABLE_NAME = "products_categories";
    }

    public ProductCategoryEntity(ProductCategoryKey key) {
        this.productCategoryKey = key;
    }

    @EmbeddedId
    private ProductCategoryKey productCategoryKey = new ProductCategoryKey();

    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_id"))
    @MapsId(ProductCategoryKey.METADATA.COLUMN_NAME_PRODUCT_ID)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private ProductEntity product;

    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_category_id"))
    @MapsId(ProductCategoryKey.METADATA.COLUMN_NAME_CATEGORY_ID)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private CategoryEntity category;

    public ProductCategoryRelationDto prepareToClient() {
        return new ProductCategoryRelationDto();
    }
}
