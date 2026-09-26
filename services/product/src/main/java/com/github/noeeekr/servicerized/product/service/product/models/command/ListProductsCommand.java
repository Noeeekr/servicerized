package com.github.noeeekr.servicerized.product.service.product.models.command;

import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilter;
import java.util.List;

public class ListProductsCommand implements ListProductsCommandInterface {
    private List<ListFilter> filters;
    private int limit;

    @Override
    public List<ListFilter> getFilters() {
        return this.filters;
    }

    @Override
    public int getLimit() {
        return this.limit;
    }
}
