package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

/**
 * ProductRepositoryFilterUtil provides query filters.
 */
interface ProductRepositoryQueryFilterBuilder extends RepositoryHelperInterface {
    //
    // Helper functions
    //
    public default <RootEntity> Predicate getProductFiltersFor(
            From<RootEntity, ProductEntity> product,
            ProductFilterableFieldsInterface productFilter) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        List<Predicate> conditions = new ArrayList<>();

        if (!productFilter.getProductName().isEmpty()
                && !productFilter.getProductName().isBlank()) {
            Predicate predicate = product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME)
                    .equalTo(productFilter.getProductName());
            conditions.add(predicate);
        }

        if (productFilter.getProductId() != null) {
            Predicate predicate = product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
                    .equalTo(productFilter.getProductId());
            conditions.add(predicate);
        }

        if (productFilter.getProductOwnerId() != null) {
            Predicate predicate = product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_OWNER_ID)
                    .equalTo(productFilter.getProductOwnerId());
            conditions.add(predicate);
        }

        return criteria.and(conditions);
    }

    public default <RootEntity> Predicate getCategoryFiltersFor(
            From<RootEntity, CategoryEntity> category,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        categoryFilters.removeIf((filter) -> {
            return filter.getCategoryName() == null && filter.getCategoryName().isEmpty();
        });

        List<Predicate> categoryConditions = new ArrayList<>();
        categoryFilters.forEach((filter) -> {
            Predicate predicate = criteria.equal(
                    category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                    filter.getCategoryName());
            categoryConditions.add(predicate);
        });

        return criteria.or(categoryConditions);
    }
}
