package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class VirtualProductRepositoryQueryBuilder {

    @Autowired
    private VirtualProductRepositoryQueryFilterBuilder filterBuilder;

    @PersistenceContext
    private EntityManager entityManager;

    public Response<CriteriaUpdate<VirtualProductEntity>> buildUpdateSingleQuery(
            UpdateVirtualProductFieldsInterface virtualProductFields,
            VirtualProductFilterableFieldsInterface virtualProductFilter) {
        // section --- business logic validation
        Response<?> response;
        {
            Integer maxWeeklyProvisionHours = virtualProductFields.getMaxWeeklyProvisionHours();
            response = VirtualProductEntity.isMaxWeeklyProvisionHoursValid(maxWeeklyProvisionHours);
            if (!response.isSuccess()) {
                return Response.fromFailure(response);
            }
        }
        {
            Integer provisionHours = virtualProductFields.getProvisionHours();
            response = VirtualProductEntity.isProvisionHoursValid(provisionHours);
            if (!response.isSuccess()) {
                return Response.fromFailure(response);
            }
        }

        // section --- query build

        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaUpdate<VirtualProductEntity> query =
                criteria.createCriteriaUpdate(VirtualProductEntity.class);

        Root<VirtualProductEntity> product = query.from(VirtualProductEntity.class);

        // section --- update filters

        List<Predicate> conditions = new ArrayList<>();
        if (virtualProductFilter != null) {
            Predicate condition =
                    this.filterBuilder.getVirtualProductFilterFor(product, virtualProductFilter);
            conditions.add(condition);
        }
        query.where(criteria.and(conditions));

        // section --- update set statement

        {
            Integer maxWeeklyProvisionHours = virtualProductFields.getMaxWeeklyProvisionHours();
            if (maxWeeklyProvisionHours != null) {
                query.set(product
                        .get(VirtualProductEntity.METADATA.COLUMN_NAME_MAX_WEEKLY_PROVISION_HOURS),
                        maxWeeklyProvisionHours);
            }
        }
        {
            Integer provisionHours = virtualProductFields.getProvisionHours();
            if (provisionHours != null) {
                query.set(product.get(VirtualProductEntity.METADATA.COLUMN_NAME_PROVISION_HOURS),
                        provisionHours);
            }
        }

        return Response.success(query);
    }
}
