package com.github.noeeekr.servicerized.product.repository;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
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
public class CategoryRepositoryTestDatabase {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public List<CategoryEntity> persistedCategories =
            List.of(new CategoryEntity(null, "000001"), new CategoryEntity(null, "000002"),
                    new CategoryEntity(null, "000003"), new CategoryEntity(null, "000004"),
                    new CategoryEntity(null, "000005"), new CategoryEntity(null, "000006"));

    // Section --- Test Database preparation

    @Transactional
    public void prepareTestDatabase() {
        Objects.requireNonNull(this.transactionManager);
        new TransactionTemplate(transactionManager).executeWithoutResult((status) -> {
            this.persistCategoriesCurrentState();
            this.persistedCategories = this.retrieveCategoriesCurrentState();
        });
    }

    public void persistCategoriesCurrentState() {
        for (int i = 0; i < this.persistedCategories.size(); i++) {
            this.entityManager.persist(this.persistedCategories.get(i));
        }
        this.entityManager.flush();
    }

    public List<CategoryEntity> retrieveCategoriesCurrentState() {
        List<String> categoriesByName =
                this.persistedCategories.stream().<String>map((category) -> {
                    return category.getCategoryName();
                }).toList();

        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> category = query.from(CategoryEntity.class);

        query.select(category)
                .where(category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME)
                        .in(categoriesByName))
                .orderBy(List.of(criteria
                        .asc(category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME))));

        return this.entityManager.createQuery(query)
                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();
    }

}
