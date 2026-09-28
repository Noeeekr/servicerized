package com.github.noeeekr.servicerized.product.repository;

import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.models.VirtualProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class VirtualProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //
    // Create queries
    //
    public Response<VirtualProductEntity> persist(VirtualProductEntity productServiceData) {
        try {
            entityManager.persist(productServiceData);
            entityManager.flush();
            return Response.success(productServiceData);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
