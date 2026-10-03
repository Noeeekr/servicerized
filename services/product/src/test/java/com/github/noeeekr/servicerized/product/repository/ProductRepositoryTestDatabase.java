package com.github.noeeekr.servicerized.product.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Component
@Transactional
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTestDatabase {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<ProductEntity> localProducts = Arrays.asList(
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000001", 0, "000001"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000002", 0, "000002"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000003", 0, "000003"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000004", 0, "000004"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000005", 0, "000005"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000006", 0, "000006"));

    private List<ProductEntity> persistedProducts = Arrays.asList(
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000007", 0, "000007"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000008", 0, "000008"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000009", 0, "000009"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000010", 0, "000010"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000011", 0, "000011"),
            new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000012", 0, "000012"));

    // Section -- Prepare database

    @BeforeAll
    public void prepareTestDatabase() {
        Objects.requireNonNull(this.transactionManager);

        new TransactionTemplate(this.transactionManager).executeWithoutResult((status) -> {
            this.prepareDatabaseCategories(this.entityManager);
            this.persistedProducts = this.getPrepareDatabaseCategoriesResults();
        });
    }

    public void prepareDatabaseCategories(EntityManager entityManager) {
        for (int i = 0; i < this.persistedProducts.size(); i++) {
            this.entityManager.persist(this.persistedProducts.get(i));
        }
        this.entityManager.flush();
    }

    public List<ProductEntity> getPrepareDatabaseCategoriesResults() {
        List<String> categoriesByName = this.persistedProducts.stream().<String>map((product) -> {
            return product.getProductName();
        }).toList();

        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = criteria.createQuery(ProductEntity.class);
        Root<ProductEntity> product = query.from(ProductEntity.class);

        query.select(product)
                .where(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME)
                        .in(categoriesByName))
                .orderBy(List.of(criteria
                        .asc(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME))));

        return this.entityManager.createQuery(query)
                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();
    }
}
