package com.github.noeeekr.servicerized.product.service.product.models.filters;

import java.util.List;
import java.util.Optional;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.ProductFilterableFieldsInterface;

/**
 * ListFilter defines all the behaviour allowed for the locals that requires product listing
 * filters. Examples of such locals are services and all the points across the code that create
 * filters to pass to them.
 */
public interface ListFilterInterface {
    public Optional<List<CategoryFilterableFieldsInterface>> getCategoryFilter();
    public Optional<ProductFilterableFieldsInterface> getProductFilter();
}
