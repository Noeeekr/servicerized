package com.github.noeeekr.servicerized.product.controller.models.request.list;

import com.github.noeeekr.servicerized.product.service.product.models.filters.CategoryFilter;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListProductRequest {
    public ProductFilter productFilter;
    public CategoryFilter[] categoryFilter;
}
