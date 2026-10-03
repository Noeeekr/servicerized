package com.github.noeeekr.servicerized.product.repository.databases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import jakarta.persistence.EntityManager;

@Component
public class CategoryRepositoryTestDatabaseFactory {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    public CategoryRepositoryTestDatabase New() {
        return new CategoryRepositoryTestDatabase(transactionTemplate, entityManager);
    }

}
