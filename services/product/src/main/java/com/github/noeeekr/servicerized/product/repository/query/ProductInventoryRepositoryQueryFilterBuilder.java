package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductInventoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

@Component
public class ProductInventoryRepositoryQueryFilterBuilder {
    @PersistenceContext
    private EntityManager entityManager;

    public <RootEntity> Predicate getProductInventoryFilter(
            From<RootEntity, ProductInventoryEntity> productInventory,
            ProductInventoryFilterableFieldsInterface productInventoryFilter) {

        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();

        List<Predicate> conditions = new ArrayList<>();

        if (productInventoryFilter.getProductId() != null) {
            conditions.add(criteria.equal(
                    productInventory.get(ProductInventoryEntity.METADATA.COLUMN_NAME_PRODUCT_ID),
                    productInventoryFilter.getProductId()));
        }

        return criteria.and(conditions);
    }
}
