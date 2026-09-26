package com.github.noeeekr.servicerized.product.controller.models.request;

import java.util.UUID;

public interface CreateProductCategoryRelationRequestInterface {
    public UUID getProductId();

    public UUID getCategoryId();
}
