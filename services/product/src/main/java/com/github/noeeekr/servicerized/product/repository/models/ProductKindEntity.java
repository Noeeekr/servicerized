package com.github.noeeekr.servicerized.product.repository.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
        public static final String TABLE_NAME = "product_kinds";

        public static final String COLUMN_NAME_PRODUCT_ID = "productId";
        
        public static final String DATABASE_COLUMN_NAME_PRODUCT_ID = "product_id";
    }

    public static class Default {
        public static final ProductKindEntity getVirtualServiceKind() {
            return new ProductKindEntity(Long.valueOf(0), "virtual-service");
        }
    }

    //
    // Constructors
    //
    public ProductKindEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public ProductKindEntity() {
        this.name = null;
        this.id = null;
    }

    //
    // Fields Containing Indexes & Primary Key
    //

    @Id
    @Column(name = ProductKindEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID, nullable = false,
            unique = true)
    private Long id;

    //
    // Primitive Fields
    //

    @Column(name = "product_kind_name", nullable = false, unique = true)
    private String name;
}


