package com.github.noeeekr.servicerized.product.repository.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface ProductRepositoryQueryBuilder extends ProductRepositoryQueryFilterBuilder {
    public default CriteriaQuery<ProductEntity> buildListProductQuery(
            ProductFilterableFieldsInterface productFilter,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = criteria.createQuery(ProductEntity.class);
        Root<ProductCategoryEntity> relationRoot = query.from(ProductCategoryEntity.class);

        List<Predicate> conditions = new ArrayList<>();

        if (categoryFilters != null && categoryFilters.isEmpty() != false) {
            conditions.add(this.getCategoryFiltersFor(relationRoot, categoryFilters));
        }
        if (productFilter != null) {
            conditions.add(this.getProductFiltersFor(relationRoot, productFilter));
        }

        conditions.add(relationRoot.get("deletedAt").isNull());

        Join<ProductCategoryEntity, ProductEntity> productJoin = relationRoot.join("product");

        return query.select(productJoin).where(criteria.and(conditions));
    }

    public default CriteriaUpdate<ProductEntity> buildMarkProductDeletedQuery(
            ProductFilterableFieldsInterface filter) {

        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<ProductEntity> query = criteria.createCriteriaUpdate(ProductEntity.class);

        Root<ProductEntity> productRoot = query.from(ProductEntity.class);

        query.set(productRoot.get(ProductEntity.METADATA.COLUMN_NAME_DELETED_AT),
                LocalDateTime.now());

        List<Predicate> conditions = new ArrayList<>();
        if (filter.getProductId() != null) {
            conditions.add(productRoot.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
                    .equalTo(filter.getProductId()));
        }
        if (filter.getProductName() != null) {
            conditions.add(productRoot.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME)
                    .equalTo(filter.getProductName()));
        }
        if (filter.getProductOwnerId() != null) {
            conditions.add(productRoot.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_OWNER_ID)
                    .equalTo(filter.getProductOwnerId()));
        }

        return query.where(criteria.or(conditions));
    }
}
