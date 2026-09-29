package com.github.noeeekr.servicerized.product.service.category.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.CategoryInterface;

public record ListCategoryCommand(UUID categoryId, String categoryName, int limit)
        implements CategoryInterface {
    @Override
    public UUID getCategoryId() {
        return this.categoryId;
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }

    public int getLimit() {
        return this.limit;
    }

    public boolean categoryIdExists() {
        return this.categoryId != null;
    }

    public boolean categoryNameExists() {
        return this.categoryName != null && this.categoryName != "";
    }
}
