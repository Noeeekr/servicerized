package com.github.noeeekr.servicerized.product.repository.databases;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

@Getter 
public class CategoryRepositoryTestDatabase {

    private TransactionTemplate transactionTemplate;

    private EntityManager entityManager;

    public List<CategoryEntity> persistedCategories;

    public CategoryRepositoryTestDatabase(TransactionTemplate transactionTemplate,
            EntityManager entityManager) {
        this.transactionTemplate = transactionTemplate;

        this.entityManager = entityManager;

        this.persistedCategories =
                List.of(new CategoryEntity(null, "000001"), new CategoryEntity(null, "000002"),
                        new CategoryEntity(null, "000003"), new CategoryEntity(null, "000004"),
                        new CategoryEntity(null, "000005"), new CategoryEntity(null, "000006"));
    }

    // Section --- Test Database preparation

    public void prepareTestDatabase() {
        this.transactionTemplate.executeWithoutResult(status -> {
            this.persistCategoriesCurrentState(this.persistedCategories);
            this.persistedCategories = this.retrieveCategoriesCurrentState();
        });
    }

    public void persistCategoriesCurrentState(List<CategoryEntity> categories) {
        for (int i = 0; i < categories.size(); i++) {
            this.entityManager.persist(categories.get(i));
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

    /**
     * findPersistedById() iterates over persisted categories and returns the category that matches
     * the providen Id. If none are found returns null.
     * 
     * @param categoryId the id of the target category.
     * 
     * @return CategoryEntity or null
     */
    public CategoryEntity findPersistedById(UUID categoryId) {
        CategoryEntity uncheckedCategory;
        for (int i = 0; i < this.persistedCategories.size(); i++) {
            uncheckedCategory = this.persistedCategories.get(i);
            if (uncheckedCategory.getCategoryId() == categoryId)
                return uncheckedCategory;
        }
        return null;
    }
}
