package com.github.noeeekr.servicerized.product.repository.models;

import com.github.noeeekr.servicerized.product.repository.interfaces.entities.KindInterface;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

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
@Getter 
@Entity
@Table(name = KindEntity.METADATA.TABLE_NAME)
public class KindEntity implements KindInterface {
    public static class METADATA {
        public static final String TABLE_NAME = "product_kinds";

        public static final String COLUMN_NAME_KIND_ID = "kindId";
        public static final String COLUMN_NAME_KIND_NAME = "kindName";

        public static final String DATABASE_COLUMN_NAME_KIND_ID = "product_kind_id";
        public static final String DATABASE_COLUMN_NAME_KIND_NAME = "product_kind_name";
    }

    public static class Default {
        public static final KindEntity getVirtualServiceKind() {
            return new KindEntity(Long.valueOf(0), "virtual-service");
        }
    }

    //
    // Constructors
    //
    public KindEntity(Long kindId, String kindName) {
        this.kindName = kindName;
        this.kindId = kindId;
    }

    public KindEntity() {
        this.kindName = null;
        this.kindId = null;
    }

    //
    // Fields Containing Indexes & Primary Key
    //

    @Id
    @Column(name = KindEntity.METADATA.DATABASE_COLUMN_NAME_KIND_ID, nullable = false,
            unique = true)
    private Long kindId;

    //
    // Primitive Fields
    //

    @Column(name = KindEntity.METADATA.DATABASE_COLUMN_NAME_KIND_NAME, nullable = false,
            unique = true)
    private String kindName;
}


