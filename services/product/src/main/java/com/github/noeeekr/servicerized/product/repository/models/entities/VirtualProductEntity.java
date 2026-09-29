package com.github.noeeekr.servicerized.product.repository.models.entities;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * VirtualProduct contains specific data related to the service kind for a generic product entity.
 */
@Getter
@Entity
@Table(name = VirtualProductEntity.METADATA.TABLE_NAME)
public class VirtualProductEntity {
    public static final class METADATA {
        public static final String TABLE_NAME = "services";
    }

    @Id
    private UUID virtualProductId;

    /**
     * Defines the amount of time the service provider believes this service takes to be completed
     * from the moment it starts.
     */
    private int provisionHours;

    /**
     * The product this entity data increments.
     */
    @MapsId
    @OneToOne
    @JoinColumn(name = ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID,
            foreignKey = @ForeignKey(name = "fk_product_id"))
    private ProductEntity product;

    public VirtualProductEntity(UUID virtualProductId, int provisionHours) {
        this.virtualProductId = virtualProductId;
        this.provisionHours = provisionHours;
    }
}
