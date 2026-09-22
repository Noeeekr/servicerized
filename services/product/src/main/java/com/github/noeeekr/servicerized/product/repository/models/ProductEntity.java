package com.github.noeeekr.servicerized.product.repository.models;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.ProductInterface;
import com.github.noeeekr.servicerized.repository.models.Metrics;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity extends Metrics implements ProductInterface {
    /**
     * Fields
     */

    // Fields - Indexes

    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "product_id")
    public UUID id;

    // Fields - Foreign Keys

    @Column(name = "product_owner_id", nullable = false)
    public UUID ownerId;

    // Fields - Primitives

    @Column(name = "product_name", nullable = false)
    public String name;

    @Column(name = "product_price", nullable = false)
    public Integer price;

    @Column(name = "product_description", nullable = true)
    public String description;

    public static ProductEntity from(CreateProductInterface product) {
        return ProductEntity.builder().name(product.getName()).ownerId(product.getOwnerId())
                .description(product.getDescription()).price(product.getPrice()).build();
    }

    public static ProductEntity from(CreateProductRequestInterface product) {
        return ProductEntity.builder().name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).build();
    }
}

/**
 * Possible future updated related to this model: 1. Add product images, but since it doesn't have
 * an visual interface, it will be skipped. 2. Add product ratings.
 */
