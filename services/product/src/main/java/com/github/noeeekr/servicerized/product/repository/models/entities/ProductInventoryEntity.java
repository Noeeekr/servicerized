package com.github.noeeekr.servicerized.product.repository.models.entities;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ProductInventoryEntity defines the format of a persisted product inventory entity according to
 * business rules. It also serves as a model for jakarta persistence through hibernate ORM.
 * 
 * 
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = ProductInventoryEntity.METADATA.DATABASE_TABLE_NAME)
public class ProductInventoryEntity {
    /**
     * METADATA holds database fields names, code column names and other constants that may be used
     * across codebase for consistency.
     */
    public static class METADATA {
        public static final String TABLE_NAME = "ProductInventoryEntity";
        public static final String DATABASE_TABLE_NAME = "product_inventories";

        public static final String COLUMN_NAME_PRODUCT_ID = "productId";
        public static final String COLUMN_NAME_PRODUCT_AVAILABLE_AMOUNT = "productAvailableAmount";

        public static final String DATABASE_COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String DATABASE_COLUMN_NAME_PRODUCT_AVAILABLE_AMOUNT =
                "product_available_amount";

    }

    // Section --- Primitive Fields

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID productId;

    @Column
    private int productAvailableAmount;

    // Section --- Join Fields

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId(ProductInventoryEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
    @JoinColumn(name = ProductInventoryEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_product_inventory_id"))
    private ProductEntity product;
}
