package com.github.noeeekr.servicerized.product.repository.databases;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryKey;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductCategoryRepositoryTestDatabase {
    private TransactionTemplate transactionTemplate;

    private EntityManager entityManager;

    private List<ProductCategoryEntity> persistedRelations = List.of();

    @Autowired
    public ProductCategoryRepositoryTestDatabase(TransactionTemplate transactionTemplate,
            EntityManager entityManager, List<ProductCategoryEntity> relations) {
        this.transactionTemplate = transactionTemplate;
        this.entityManager = entityManager;
    }

    public static List<ProductCategoryEntity> getRelations(
            ProductRepositoryTestDatabase productTestDatabase,
            CategoryRepositoryTestDatabase categoryTestDatabase) throws AssertionError {
        if (productTestDatabase.getPersistedProducts().size() < 6) {
            fail("Unable to proceed: Minimum amount of persisted products (6) not met.");
            return null;
        }

        if (categoryTestDatabase.getPersistedCategories().size() < 6) {
            fail("Unable to proceed: Minimum amount of persisted categories (6) not met.");
            return null;
        }

        List<ProductEntity> products = productTestDatabase.getPersistedProducts();
        List<CategoryEntity> categories = categoryTestDatabase.getPersistedCategories();

        List<ProductCategoryEntity> productCategoryRelations = new ArrayList<>();

        /**
         * First half of the products have relations
         */
        products.subList(0, (products.size() / 2) + 1).forEach((product) -> {
            categories.forEach((category) -> {
                productCategoryRelations.add(new ProductCategoryEntity(product, category));
            });
        });

        return productCategoryRelations;
    }

    public void prepareTestDatabase(List<ProductCategoryEntity> relations) {
        transactionTemplate.executeWithoutResult((status) -> {
            this.persistRelationsState(relations);
            this.persistedRelations = this.retrieveRelationsState(relations);
        });
    }

    public void persistRelationsState(List<ProductCategoryEntity> relations) {
        for (int i = 0; i < relations.size(); i++) {
            this.entityManager.persist(relations.get(i));
        }
        this.entityManager.flush();
    }

    public List<ProductCategoryEntity> getPersistedRelations() {
        return this.persistedRelations;
    }

    public List<ProductCategoryEntity> retrieveRelationsState() {
        return this.retrieveRelationsState(this.persistedRelations);
    }

    public List<ProductCategoryEntity> retrieveRelationsState(
            List<ProductCategoryEntity> targetRelations) {
        List<ProductCategoryKey> keys =
                targetRelations.stream().<ProductCategoryKey>map((relation) -> {
                    return relation.getProductCategoryKey();
                }).toList();


        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductCategoryEntity> query =
                criteria.createQuery(ProductCategoryEntity.class);
        Root<ProductCategoryEntity> relation = query.from(ProductCategoryEntity.class);

        List<Predicate> conditions = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            ProductCategoryKey key = keys.get(i);

            conditions.add(criteria.and(

                    criteria.equal(relation

                            .get(ProductCategoryEntity.METADATA.COLUMN_NAME_PRODUCT_CATEGORY_KEY)
                            .get(ProductCategoryKey.METADATA.COLUMN_NAME_PRODUCT_ID),
                            key.getProductId()),

                    criteria.equal(relation

                            .get(ProductCategoryEntity.METADATA.COLUMN_NAME_PRODUCT_CATEGORY_KEY)
                            .get(ProductCategoryKey.METADATA.COLUMN_NAME_CATEGORY_ID),
                            key.getCategoryId())));
        }

        query.select(relation).where(criteria.or(conditions))
                .orderBy(List.of(criteria.asc(relation
                        .get(ProductCategoryEntity.METADATA.COLUMN_NAME_PRODUCT_CATEGORY_KEY)
                        .get(ProductCategoryKey.METADATA.COLUMN_NAME_PRODUCT_ID))));

        List<ProductCategoryEntity> relations = this.entityManager.createQuery(query)
                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();

        return relations;
    }
}
