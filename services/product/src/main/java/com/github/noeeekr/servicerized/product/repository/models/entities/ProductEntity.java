package com.github.noeeekr.servicerized.product.repository.models.entities;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.ProductInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.CreateVirtualProductInterface;
import com.github.noeeekr.servicerized.product.repository.models.dto.ProductDto;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateProductCommandInterface;
import com.github.noeeekr.servicerized.repository.models.MetricsEntity;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * ProductEntity defines the format of data for a user product.
 * 
 * <br/>
 * 
 * 'Product' entity is intended to hold information about a 'service' that is available to be rented
 * by a user. For this purpose it should demonstrate the following behaviour:
 * 
 * <br/>
 * 
 * 1. The field 'deleted_at' (extended from Metrics) from this entity defines privative behavior.
 * Any entity instance where this field is not null should be considered private for its owner and
 * be ignored by default CRUD queries unless their domain rules strictly target them.
 * 
 * <br/>
 * 
 * 2. This entity should not define data related to actual processes that may act upon products, it
 * should only act as a registry for an available 'service'. If necessary, create other entities for
 * that purpose.
 * 
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = ProductEntity.METADATA.TABLE_NAME, schema = Models.SCHEMA)
public class ProductEntity extends MetricsEntity implements ProductInterface, ClientResponseDto {
    /**
     * METADATA defines a single source of truth for external references to this table names to be
     * used across this micro-service, providing consistency and easy maintence.
     */
    public static final class METADATA {
        public static final String TABLE_NAME = "products";

        public static final String COLUMN_NAME_PRODUCT_ID = "id";

        public static final String DATABASE_COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String DATABASE_COLUMN_NAME_PRODUCT_OWNER_ID = "product_owner_id";
    }

    //
    // Fields - Indexes
    //

    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = ProductEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_ID)
    public UUID id;

    //
    // Fields - Foreign Keys
    //

    @Column(name = ProductEntity.METADATA.DATABASE_COLUMN_NAME_PRODUCT_OWNER_ID, nullable = false)
    public UUID ownerId;

    //
    // Fields - Primitives
    //

    @Column(name = "product_name", nullable = false, unique = true)
    public String name;

    @Column(name = "product_price", nullable = false)
    public Integer price;

    @Column(name = "product_description", nullable = true)
    public String description;

    //
    // Transformators
    //

    public static ProductEntity from(CreateProductRequestInterface product) {
        return ProductEntity.builder().name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).build();
    }

    public static ProductEntity from(CreateProductCommandInterface product) {
        return ProductEntity.builder().name(product.getName()).ownerId(product.getOwnerId())
                .description(product.getDescription()).price(product.getPrice()).build();
    }

    public static ProductEntity from(CreateVirtualProductInterface product) {
        return ProductEntity.builder().name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).build();
    }


    //
    // Interface implementations
    //

    public ProductInterface prepareToClient() {
        return ProductDto.from(this);
    }
}

/**
 * Possible future updated related to this model: 1. Add product images, but since it doesn't have
 * an visual interface, it will be skipped. 2. Add product ratings.
 */
