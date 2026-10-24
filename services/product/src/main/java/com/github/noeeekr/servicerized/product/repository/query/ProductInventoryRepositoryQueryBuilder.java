package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductInventoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class ProductInventoryRepositoryQueryBuilder {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductInventoryRepositoryQueryFilterBuilder filter;

    public CriteriaQuery<ProductInventoryEntity> buildListSingleQuery(
            ProductInventoryFilterableFieldsInterface productInventoryFilter) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();

        CriteriaQuery<ProductInventoryEntity> productInventoryQuery =
                criteria.createQuery(ProductInventoryEntity.class);

        Root<ProductInventoryEntity> productInventoryRoot =
                productInventoryQuery.from(ProductInventoryEntity.class);
        productInventoryQuery.select(productInventoryRoot.get("ProductInventoryEntity"));

        List<Predicate> conditions = new ArrayList<>();

        if (productInventoryFilter != null && !productInventoryFilter.isEmpty()) {
            conditions.add(
                    filter.getProductInventoryFilter(productInventoryRoot, productInventoryFilter));
        }

        return productInventoryQuery.where(criteria.and(conditions));
    }

    // private CriteriaQuery<ProductInventoryEntity> updateSingleQuery(
    // ProductInventoryUpdatableFieldsInterface inventoryUpdatableFields,
    // ProductInventoryFilterableFieldsInterface inventoryFilter) {
    // }
}
