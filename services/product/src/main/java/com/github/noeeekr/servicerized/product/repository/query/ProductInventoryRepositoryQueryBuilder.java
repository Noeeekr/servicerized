package com.github.noeeekr.servicerized.product.repository.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.noeeekr.servicerized.product.failures.ServiceFailures;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductInventoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.operations.UpdateProductInventoryFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductInventoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @deprecated Currently, there is no need for this class, only {@link VirtualProductEntity} is
 *             available, which manages its inventory itself. Still, it is kept for future updates.
 */
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

    public Response<CriteriaUpdate<ProductInventoryEntity>> buildUpdateSingleQuery(
            UpdateProductInventoryFieldsInterface updateProductInventory,
            ProductInventoryFilterableFieldsInterface productInventoryFilter) {
        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaUpdate<ProductInventoryEntity> query =
                criteria.createCriteriaUpdate(ProductInventoryEntity.class);

        Root<ProductInventoryEntity> productInventory = query.from(ProductInventoryEntity.class);

        {
            Integer availableAmount = updateProductInventory.getProductAvailableAmount();
            if (0 > availableAmount) {
                return Response.fromFailure(new ServiceFailures.BadRequest(
                        "os filtros requerem o campo 'quantidade disponível' >= 0 ao envés de 'quantidade disponível' < 0"));
            }
            if (availableAmount != null) {
                query.set(productInventory
                        .get(ProductInventoryEntity.METADATA.COLUMN_NAME_PRODUCT_AVAILABLE_AMOUNT),
                        availableAmount);
            }
        }

        Predicate condition =
                this.filter.getProductInventoryFilter(productInventory, productInventoryFilter);
        query.where(condition);

        return Response.success(query);
    }
}
