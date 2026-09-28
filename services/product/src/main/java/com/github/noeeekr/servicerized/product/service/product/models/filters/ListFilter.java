package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.List;
import java.util.Optional;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import lombok.AllArgsConstructor;

/**
 * ListFilter is the default implementation for ListFilterInterface.
 */
@AllArgsConstructor
public class ListFilter implements ListFilterInterface {
    /**
     * Allows filtering by category relation
     */
    private List<CategoryFilterableFieldsInterface> categoryFilter;
    private ProductFilterableFieldsInterface productFilter;

    public Optional<List<CategoryFilterableFieldsInterface>> getCategoryFilter() {
        return Optional.of(this.categoryFilter);
    }

    public Optional<ProductFilterableFieldsInterface> getProductFilter() {
        return Optional.of(this.productFilter);
    }
}
