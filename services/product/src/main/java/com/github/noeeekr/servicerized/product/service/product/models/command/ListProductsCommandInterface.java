package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.List;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilter;

public interface ListProductsCommandInterface {
    public List<ListFilter> getFilters();
    public int getLimit();
}
