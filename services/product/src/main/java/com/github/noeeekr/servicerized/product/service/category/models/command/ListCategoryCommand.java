package com.github.noeeekr.servicerized.product.service.category.models.command;

import java.util.UUID;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.CategoryInterface;

public record ListCategoryCommand(UUID categoryId, String categoryName)
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
