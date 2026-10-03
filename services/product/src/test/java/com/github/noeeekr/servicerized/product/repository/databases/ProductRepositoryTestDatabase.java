package com.github.noeeekr.servicerized.product.repository.databases;

import java.util.Arrays;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

@Getter
public class ProductRepositoryTestDatabase {
    private EntityManager entityManager;
    private ProductRepository productRepository;
    private TransactionTemplate transactionTemplate;
    private PlatformTransactionManager transactionManager;

    private List<ProductEntity> localProducts;
    private List<ProductEntity> persistedProducts;

    public ProductRepositoryTestDatabase(TransactionTemplate transactionTemplate,
            EntityManager entityManager, ProductRepository productRepository,
            PlatformTransactionManager transactionManager) {
        this.entityManager = entityManager;
        this.productRepository = productRepository;
        this.transactionManager = transactionManager;
        this.transactionTemplate = transactionTemplate;
        this.localProducts = Arrays.asList(
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000001", 0, "000001"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000002", 0, "000002"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000003", 0, "000003"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000004", 0, "000004"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000005", 0, "000005"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000006", 0, "000006"));

        this.persistedProducts = Arrays.asList(
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000007", 0, "000007"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000008", 0, "000008"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000009", 0, "000009"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000010", 0, "000010"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000011", 0, "000011"),
                new ProductEntity(null, UuidCreator.getTimeOrderedEpoch(), "000012", 0, "000012"));
    }

    // Section -- Prepare database

    public void prepareTestDatabase() {
        this.transactionTemplate.executeWithoutResult(status -> {
            this.persistProducts(this.persistedProducts);
            this.persistedProducts = this.getPersistedProducts();
        });
    }

    public void persistProducts(List<ProductEntity> products) {
        for (int i = 0; i < products.size(); i++) {
            this.entityManager.persist(products.get(i));
        }
        this.entityManager.flush();
    }

    public List<ProductEntity> getPersistedProducts() {
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
