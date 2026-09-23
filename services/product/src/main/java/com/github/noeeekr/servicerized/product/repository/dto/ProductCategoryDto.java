package com.github.noeeekr.servicerized.product.repository.dto;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.ProductCategoryInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDto implements ProductCategoryInterface {
    private UUID productId;
    private UUID categoryId;

    public UUID getProductId() {
        return this.productId;
    };

    public UUID getCategoryId() {
        return this.categoryId;
    };
}
