package com.github.noeeekr.servicerized.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles({"in-memory-db"})
public class CategoryRepositoryUnitTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private static List<CategoryEntity> targetCategories =
            List.of(new CategoryEntity(null, "000001"), new CategoryEntity(null, "000002"),
                    new CategoryEntity(null, "000003"), new CategoryEntity(null, "000004"),
                    new CategoryEntity(null, "000005"), new CategoryEntity(null, "000006"));

    // Section --- Test preparation

    @BeforeAll
    @Transactional
    public static void prepareTestDatabase(@Autowired EntityManager entityManager,
            @Autowired PlatformTransactionManager transactionManager) {
        Objects.requireNonNull(transactionManager);
        new TransactionTemplate(transactionManager).executeWithoutResult((status) -> {
            CategoryRepositoryUnitTest.prepareDatabaseCategories(entityManager);
            CategoryRepositoryUnitTest.targetCategories =
                    CategoryRepositoryUnitTest.getPrepareDatabaseCategoriesResults(entityManager);
        });
    }

    public static void prepareDatabaseCategories(EntityManager entityManager) {
        for (int i = 0; i < CategoryRepositoryUnitTest.targetCategories.size(); i++) {
            entityManager.persist(CategoryRepositoryUnitTest.targetCategories.get(i));
        }
    }

    public static List<CategoryEntity> getPrepareDatabaseCategoriesResults(
            EntityManager entityManager) {
        List<String> categoriesByName =
                CategoryRepositoryUnitTest.targetCategories.stream().<String>map((category) -> {
                    return category.getCategoryName();
                }).toList();

        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> category = query.from(CategoryEntity.class);

        query.select(category)
                .where(category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME)
                        .in(categoriesByName))
                .orderBy(List.of(criteria
                        .asc(category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME))));

        return entityManager.createQuery(query)
                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();
    }

    // Section --- Test Find Category By Id

    @Test
    public void findCategoryByIdTest() {
        CategoryEntity category = this.arrangeFindCategoryByIdTest();
        Response<List<CategoryEntity>> response = this.executeFindCategoryByIdTest(category);
        this.validateFindCategoryByIdTest(response, category);
    }

    public CategoryEntity arrangeFindCategoryByIdTest() {
        /**
         * Create some test categories
         */
        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> category = query.from(CategoryEntity.class);
        query.select(category)
                .where(criteria.equal(
                        category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                        CategoryRepositoryUnitTest.targetCategories.get(0).getCategoryName()));

        return this.entityManager.createQuery(query).getSingleResult();
    }

    public Response<List<CategoryEntity>> executeFindCategoryByIdTest(CategoryEntity category) {
        return categoryRepository.findCategory(category.getCategoryId());
    }

    public void validateFindCategoryByIdTest(Response<List<CategoryEntity>> recievedResponse,
            CategoryEntity targetEntity) {
        if (!recievedResponse.isSuccess()) {
            fail("Test Error: Bad Response. Expected: Success Response. Recieved: Failed Response. Reason: "
                    + recievedResponse.getFailure().message());
            return;
        }

        Response<List<CategoryEntity>> expectedResponse = Response.success(List.of(targetEntity));

        try {
            assertEquals(expectedResponse, recievedResponse);
        } catch (Exception e) {
            DebugLogger.displayEntity("Recieved Response", recievedResponse,
                    "Test Validator (Find Category Id Test)", "Category Repository (Unit Test)");
            DebugLogger.displayEntity("Expected Response", expectedResponse,
                    "Test Validator (Find Category Id Test)", "Category Repository (Unit Test)");
            fail("Test Failed: Response format conflict. Expected: Success Response. Recieved: Failed Response.");
        }
    }

    // @Transactional
    // public Response<List<CategoryEntity>> findCategory(UUID categoryId) {
    // CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
    // CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
    // Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

    // List<Predicate> requiredConditionals = new ArrayList<>();
    // requiredConditionals.add(criteria
    // .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
    // requiredConditionals.add(criteria.equal(
    // categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_ID), categoryId));
    // query.where(criteria.and(requiredConditionals));

    // List<CategoryEntity> categories = new ArrayList<>();
    // try {
    // categories.add(entityManager.createQuery(query).getSingleResult());
    // return Response.success(categories);
    // } catch (NoResultException e) {
    // return Response.success(categories);
    // } catch (Exception e) {
    // return Response.fromFailure(new Failures.UnhandledException(e));
    // }
    // };

    // @Transactional
    // public Response<List<CategoryEntity>> findCategories(String categoryName) {
    // CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
    // CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
    // Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

    // List<Predicate> requiredConditionals = new ArrayList<>();
    // requiredConditionals.add(criteria
    // .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
    // requiredConditionals.add(
    // criteria.like(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
    // "%" + categoryName + "%"));
    // query.where(criteria.and(requiredConditionals));

    // List<CategoryEntity> categories = new ArrayList<>();
    // try {
    // categories.addAll(entityManager.createQuery(query).getResultList());
    // return Response.success(categories);
    // } catch (NoResultException e) {
    // return Response.success(categories);
    // } catch (Exception e) {
    // return Response.fromFailure(new Failures.UnhandledException(e));
    // }
    // };

    // @Transactional
    // public Response<List<CategoryEntity>> findCategory(String categoryName, UUID categoryId) {
    // CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
    // CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
    // Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

    // List<Predicate> requiredConditionals = new ArrayList<>();
    // requiredConditionals.add(criteria
    // .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
    // requiredConditionals.add(
    // criteria.like(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
    // "%" + categoryName + "%"));
    // requiredConditionals.add(criteria.equal(
    // categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_ID), categoryId));
    // query.where(criteria.and(requiredConditionals));

    // List<CategoryEntity> categories = new ArrayList<>();
    // try {
    // categories.add(entityManager.createQuery(query).getSingleResult());
    // return Response.success(categories);
    // } catch (NoResultException e) {
    // return Response.success(categories);
    // } catch (Exception e) {
    // return Response.fromFailure(new Failures.UnhandledException(e));
    // }
    // };
}
