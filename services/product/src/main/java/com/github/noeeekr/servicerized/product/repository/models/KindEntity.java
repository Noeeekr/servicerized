package com.github.noeeekr.servicerized.product.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * ProductKindEntity is meant to store the system values for the available product kinds. A kind is
 * not a select like a category, it classifies products with different business rules that may
 * change logic implementation across service.
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
@Table(name = KindEntity.METADATA.TABLE_NAME)
public class KindEntity {
    public static class METADATA {
        public static final String TABLE_NAME = "product_kinds";

        public static final String COLUMN_NAME_PRODUCT_ID = "productId";

        public static final String DATABASE_COLUMN_NAME_PRODUCT_ID = "product_id";
    }

    public static class Default {
        public static final KindEntity getVirtualServiceKind() {
            return new KindEntity(Long.valueOf(0), "virtual-service");
        }
    }

    //
    // Constructors
    //
    public KindEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public KindEntity() {
        this.name = null;
        this.id = null;
    }

    //
    // Fields Containing Indexes & Primary Key
    //

    @Id
    @Column(name = KindEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID, nullable = false,
            unique = true)
    private Long id;

    //
    // Primitive Fields
    //

    @Column(name = "product_kind_name", nullable = false, unique = true)
    private String name;
}


