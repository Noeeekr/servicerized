package com.github.noeeekr.servicerized.product.repository.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
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
            Join<ProductCategoryEntity, CategoryEntity> categoryJoin =
                    relationRoot.join("category");
            conditions.add(this.getCategoryFiltersFor(categoryJoin, categoryFilters));
        }
        if (productFilter != null) {
            Join<ProductCategoryEntity, ProductEntity> productJoin = relationRoot.join("product");
            conditions.add(this.getProductFiltersFor(productJoin, productFilter));
        }

        conditions.add(relationRoot.get("deletedAt").isNull());

        Join<ProductCategoryEntity, ProductEntity> productJoin = relationRoot.join("product");

        return query.select(productJoin).where(criteria.and(conditions));

    }

    public default CriteriaUpdate<ProductEntity> buildMarkProductDeletedQuery(
            ProductFilterableFieldsInterface filter) {

        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<ProductEntity> query = criteria.createCriteriaUpdate(ProductEntity.class);

        Root<ProductEntity> product = query.from(ProductEntity.class);

        query.set(product.get(ProductEntity.METADATA.COLUMN_NAME_DELETED_AT), LocalDateTime.now());

        List<Predicate> conditions = new ArrayList<>();
        if (filter != null && filter.isEmpty() != false) {
            conditions.add(this.getProductFiltersFor(product, filter));
        }
        return query.where(criteria.or(conditions));
    }

    public default CriteriaUpdate<ProductEntity> buildUpdateProductQuery(
            UpdateProductFieldsInterface productFields, ProductFilterableFieldsInterface filter) {

        CriteriaBuilder criteria = this.getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<ProductEntity> query = criteria.createCriteriaUpdate(ProductEntity.class);

        Root<ProductEntity> product = query.from(ProductEntity.class);

        query.set(product.get(ProductEntity.METADATA.COLUMN_NAME_DELETED_AT), LocalDateTime.now());

        List<Predicate> conditions = new ArrayList<>();
        if (filter != null && filter.isEmpty() != false) {
            conditions.add(this.getProductFiltersFor(product, filter));
        }

        if (productFields.getProductName() != null && !productFields.getProductName().isBlank()) {
            query.set(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME),
                    productFields.getProductName());
        }

        if (productFields.getProductPrice() != null) {
            query.set(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_PRICE),
                    productFields.getProductPrice());
        }

        if (productFields.getProductDescription() != null
                && !productFields.getProductDescription().isBlank()) {
            query.set(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_DESCRIPTION),
                    productFields.getProductDescription());
        }

        return query.where(criteria.or(conditions));

    }
}
