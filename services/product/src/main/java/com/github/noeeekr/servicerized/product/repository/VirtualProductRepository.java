package com.github.noeeekr.servicerized.product.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.product.repository.query.VirtualProductRepositoryQueryBuilder;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaUpdate;

@Repository
public class VirtualProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VirtualProductRepositoryQueryBuilder query;

    //
    // Create queries
    //
    public Response<VirtualProductEntity> persist(VirtualProductEntity productServiceData) {
        try {
            {
                Response<VirtualProductEntity> response = productServiceData.isValid();
                if (!response.isSuccess())
                    return response;
            }

            entityManager.persist(productServiceData);
            entityManager.flush();
            return Response.success(productServiceData);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    //
    // Update queries
    //
    public Response<Integer> updateSingle(UpdateVirtualProductFieldsInterface virtualProductFields,
            VirtualProductFilterableFieldsInterface virtualProductFilter) {
        Response<CriteriaUpdate<VirtualProductEntity>> response =
                this.query.buildUpdateSingleQuery(virtualProductFields, virtualProductFilter);
        if (!response.isSuccess())
            return Response.fromFailure(response);

        try {
            Integer updatedAmount =
                    entityManager.createQuery(response.getPayload()).executeUpdate();
            return Response.success(updatedAmount);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
}
