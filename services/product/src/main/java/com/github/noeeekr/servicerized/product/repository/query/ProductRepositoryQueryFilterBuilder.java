package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
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
        Join<ProductCategoryEntity, ProductEntity> categoryJoin = relationRoot.join("category");

        if (!productFilter.getName().isEmpty() && !productFilter.getName().isBlank()) {
            Predicate predicate = criteria.equal(categoryJoin.get("name"), productFilter.getName());
            productConditions.add(predicate);
        }
        if (productFilter.getId() != null) {
            Predicate predicate = criteria.equal(categoryJoin.get("id"), productFilter.getId());
            productConditions.add(predicate);
        }

        return criteria.and(productConditions);
    }

    public default Predicate getCategoryFiltersFor(Root<ProductCategoryEntity> relationRoot,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        /**
         * Can possibly fail due to List iterator implementation
         */
        categoryFilters.removeIf((filter) -> {
            /**
             * Skips empty conditions
             */
            String categoryName = filter.getCategoryName();
            if (categoryName == null || categoryName.isEmpty())
                return true;
            return false;
        });

        List<Predicate> categoryConditions = new ArrayList<>();
        Join<ProductCategoryEntity, ProductEntity> categoryJoin = relationRoot.join("category");
        categoryFilters.forEach((filter) -> {
            Predicate predicate =
                    criteria.equal(categoryJoin.get("name"), filter.getCategoryName());
            categoryConditions.add(predicate);
        });

        return criteria.or(categoryConditions);
    }
}
