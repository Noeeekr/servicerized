package com.github.noeeekr.servicerized.product.repository.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * ServiceEntity contains specific data related to the service kind for a generic product entity.
 */
@Getter
@Entity
@Table(name = ServiceEntity.METADATA.TABLE_NAME)
public class ServiceEntity {
    public static final class METADATA {
        public static final String TABLE_NAME = "services";
    }

    @Id
    private Long id;

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
    @JoinColumn(name = ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
    private ProductEntity product;
}
