package com.github.noeeekr.servicerized.product.repository.databases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;

@Component
public class ProductRepositoryTestDatabaseFactory {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ProductRepository productRepository;

    public ProductRepositoryTestDatabase New() {
        return new ProductRepositoryTestDatabase(transactionTemplate, entityManager,
                productRepository, transactionManager);
    }

}
