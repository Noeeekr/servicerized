package com.github.noeeekr.servicerized.product.repository;

import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.ProductKindEntity;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductKindRepository {
    @PersistenceContext 
    private EntityManager entityManager;

    //
    // Create queries
    //
    public Response<ProductKindEntity> persist(ProductKindEntity relation) {
        try {
            entityManager.persist(relation);
            entityManager.flush();
            return Response.success(relation);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
