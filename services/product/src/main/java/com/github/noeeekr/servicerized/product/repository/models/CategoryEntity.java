package com.github.noeeekr.servicerized.product.repository.models;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * CategoryEntity defines the data format for a category that a product can have.
 *
 * <br/>
 * <br/>
 * 
 * Intended Behavior. This entity is intended to be used as a filter for selecting products. For
 * this it provides the following behaviors:
 * 
 * <br/>
 * 
 * 1. This entity can be attached to any product a user. owns. This is currently done through by
 * creating a relation between this table and the product entity table through a ternary table.
 * 
 * <br/>
 * 
 * 2. This entity data should be used only in queries as a WHERE filter to target specific products.
 * 
 * <br/>
 * 
 * 3. To prevent issues (like bad names), categories should be created only by system and no public
 * endpoints should be allowed to exist.
 * 
 * <br/>
 * <br/>
 * 
 * Any other use that is not defined in this rules is undocumented.
 */
@Entity
@Getter
@Table(name = CategoryEntity.METADATA.TABLE_NAME, schema = Models.SCHEMA)
public class CategoryEntity {
    public static final class METADATA {
        public static final String TABLE_NAME = "categories";

        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
    }

    @Id
    @GeneratedValue()
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "product_category_id", nullable = false, unique = true)
    public UUID categoryId;

    @Column(name = "product_category_name", nullable = false, unique = true)
    public String categoryName;
}
