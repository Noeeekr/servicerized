package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateVirtualProductFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class VirtualProductRepositoryQueryBuilder {

    @Autowired
    private VirtualProductRepositoryQueryFilterBuilder filterBuilder;

    @PersistenceContext
    private EntityManager entityManager;

    public CriteriaQuery<VirtualProductEntity> buildUpdateSingleQuery(
            UpdateVirtualProductFieldsInterface virtualProductFields,
            VirtualProductFilterableFieldsInterface virtualProductFilter) {
        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<VirtualProductEntity> query =
                criteria.createQuery(VirtualProductEntity.class);

        Root<VirtualProductEntity> product = query.from(VirtualProductEntity.class);

        List<Predicate> conditions = new ArrayList<>();
        if (virtualProductFilter != null) {
            Predicate condition =
                    this.filterBuilder.getVirtualProductFilterFor(product, virtualProductFilter);
            conditions.add(condition);
        }

        return query.where(criteria.and(conditions));
    }
}
