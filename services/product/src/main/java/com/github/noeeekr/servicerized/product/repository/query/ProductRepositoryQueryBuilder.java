package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

        return query.select(productJoin).where(conditions);
    }
}
