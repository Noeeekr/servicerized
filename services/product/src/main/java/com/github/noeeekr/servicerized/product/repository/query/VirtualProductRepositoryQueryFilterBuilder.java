package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.VirtualProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

@Component
public class VirtualProductRepositoryQueryFilterBuilder {
    //
    // Class fields
    //
    @PersistenceContext
    private EntityManager entityManager;

    //
    // Methods
    //
    public <RootEntity> Predicate getVirtualProductFilterFor(
            From<RootEntity, VirtualProductEntity> virtualProduct,
            VirtualProductFilterableFieldsInterface filter) {

        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();

        List<Predicate> conditions = new ArrayList<>();
        if (filter.getProductId() != null) {
            Predicate condition =
                    virtualProduct.get(VirtualProductEntity.METADATA.COLUMN_NAME_PRODUCT_ID)
                            .equalTo(filter.getProductId());
            conditions.add(condition);
        }

        return criteria.and(conditions);
    }
}
