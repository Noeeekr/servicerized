package com.github.noeeekr.servicerized.product.service.category.models.request;

import java.util.UUID;

/**
 * DetachCategoryRequest defines the format of a request to detach a category from a product.
 * Detaching a category from a product deletes a relation between them, this behavior is also
 * documented at Product Category Entity.
 * 
 * @param ownerId The user that wants do the operation, necessary for permission checks.
 * @param productId The product the user wants to detach the category from.
 * @param categoryId The category to detach from a product.
 */
public record DetachCategoryRequest(UUID userId, UUID productId, UUID categoryId) {

}