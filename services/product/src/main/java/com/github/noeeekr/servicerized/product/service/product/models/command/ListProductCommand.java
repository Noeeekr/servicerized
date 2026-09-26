package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.Optional;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilterInterface;

public class ListProductCommand implements ListProductCommandInterface {
    private Optional<ListFilterInterface> filter;

    public ListProductCommand(ListFilterInterface filter) {
        if (filter == null) {
            this.filter = Optional.empty(); 
        } else {
            this.filter = Optional.of(filter);
        }
    }

    @Override
    public Optional<ListFilterInterface> getFilter() {
        return this.filter;
    }
}
