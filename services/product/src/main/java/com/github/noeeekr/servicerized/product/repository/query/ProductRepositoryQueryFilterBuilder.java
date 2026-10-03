package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * ProductRepositoryFilterUtil provides query filters.
 */
interface ProductRepositoryQueryFilterBuilder extends RepositoryHelperInterface {
    //
    // Helper functions
    //
    public default Predicate getProductFiltersFor(Root<ProductCategoryEntity> relationRoot,
            ProductFilterableFieldsInterface productFilter) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        List<Predicate> productConditions = new ArrayList<>();
        Join<ProductCategoryEntity, ProductEntity> productJoin = relationRoot.join("product");

        if (!productFilter.getProductName().isEmpty()
                && !productFilter.getProductName().isBlank()) {
            Predicate predicate =
                    criteria.equal(productJoin.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME),
                            productFilter.getProductName());
            productConditions.add(predicate);
        }
        
        if (productFilter.getProductId() != null) {
            Predicate predicate =
                    criteria.equal(productJoin.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID),
                            productFilter.getProductId());
            productConditions.add(predicate);
        }

        return criteria.and(productConditions);
    }

    public default Predicate getCategoryFiltersFor(Root<ProductCategoryEntity> relationRoot,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        categoryFilters.removeIf((filter) -> {
            return filter.getCategoryName() == null && filter.getCategoryName().isEmpty();
        });

        List<Predicate> categoryConditions = new ArrayList<>();
        Join<ProductCategoryEntity, CategoryEntity> categoryJoin = relationRoot.join("category");
        categoryFilters.forEach((filter) -> {
            Predicate predicate = criteria.equal(
                    categoryJoin.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                    filter.getCategoryName());
            categoryConditions.add(predicate);
        });

        return criteria.or(categoryConditions);
    }
}
