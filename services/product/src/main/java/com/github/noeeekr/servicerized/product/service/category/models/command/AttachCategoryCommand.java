package com.github.noeeekr.servicerized.product.service.category.models.command;

import java.util.UUID;

/**
 * AttachCategoryRequest defines the format of a request to attach a category to a product.
 * Attaching a category to a product creates a relation between them, this behavior is also
 * documented at Product Category Entity.
 * 
 * @param ownerId The user that wants do the operation, necessary for permission checks.
 * @param productId The product the user wants to attach the category to.
 * @param categoryId The category to attach to a product.
 */
public record AttachCategoryCommand(UUID userId, UUID productId, UUID categoryId) {

}