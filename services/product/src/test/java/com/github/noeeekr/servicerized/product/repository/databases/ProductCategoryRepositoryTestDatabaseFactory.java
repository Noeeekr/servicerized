package com.github.noeeekr.servicerized.product.repository.databases;

import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import jakarta.persistence.EntityManager;

@Component
public class ProductCategoryRepositoryTestDatabaseFactory {

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    EntityManager entityManager;

    public ProductCategoryRepositoryTestDatabase New(
            Supplier<List<ProductCategoryEntity>> getRelations) {
        return new ProductCategoryRepositoryTestDatabase(transactionTemplate, entityManager,
                getRelations.get());
    }

}
