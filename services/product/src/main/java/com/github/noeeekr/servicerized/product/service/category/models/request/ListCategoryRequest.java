package com.github.noeeekr.servicerized.product.service.category.models.request;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.CategoryInterface;

public record ListCategoryRequest(UUID categoryId, String categoryName)
        implements CategoryInterface {
    @Override
    public UUID getCategoryId() {
        return this.categoryId;
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }
}
