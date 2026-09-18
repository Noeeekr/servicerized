package com.github.noeeekr.servicerized.product.repository.models;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.github.noeeekr.servicerized.repository.models.Metrics;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter 
@Entity
@Table(name = "products")
public class Product extends Metrics {
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

    /**
     * Could have product images, but since it doesn't have an visual interface, it will be skipped.
     */
    /**
     * Could have product ratings.
     */
}
