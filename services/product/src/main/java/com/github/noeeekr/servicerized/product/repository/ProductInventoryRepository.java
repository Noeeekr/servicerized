package com.github.noeeekr.servicerized.product.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductInventoryEntity;
import com.github.noeeekr.servicerized.product.repository.query.ProductInventoryRepositoryQueryBuilder;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;

@Repository
public class ProductInventoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductInventoryRepositoryQueryBuilder queryBuilder;

    public Response<?> persistSingle(ProductInventoryEntity inventory) {
        try {
            this.entityManager.persist(inventory);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }

        return Response.success(inventory);
    }

    public Response<ProductInventoryEntity> listSingle(
            ProductInventoryFilterableFieldsInterface filter) {
        CriteriaQuery<ProductInventoryEntity> query = queryBuilder.buildListSingleQuery(filter);

        try {
            ProductInventoryEntity productInventory =
                    this.entityManager.createQuery(query).setMaxResults(1).getSingleResult();
            return Response.success(productInventory);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
