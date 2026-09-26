package com.github.noeeekr.servicerized.product.service.product.models.command;

import java.util.List;
import java.util.Optional;
import com.github.noeeekr.servicerized.product.controller.models.request.list.ListProductRequest;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilter;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilterInterface;

public interface ListProductCommandInterface {
    default public Optional<ListFilterInterface> getFilter() {
        return Optional.empty();
    }

    //
    // Transformers
    //
    public static ListProductCommandInterface upgrade(ListProductRequest request) {
        return new ListProductCommand(
                new ListFilter(List.of(request.categoryFilter), request.productFilter));
    }
}
