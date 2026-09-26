package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.CategoryFilterableFieldsInterface;

public class CategoryFilter implements CategoryFilterableFieldsInterface {
    private UUID categoryId;
    private String categoryName;

    public CategoryFilter(CategoryFilterableFieldsInterface category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }

    public CategoryFilter(UUID categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public UUID getCategoryId() {
        return this.categoryId;
    };

    public String getCategoryName() {
        return this.categoryName;
    }
}
