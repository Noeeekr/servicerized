package com.github.noeeekr.servicerized.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"in-memory-db"})
public class CategoryRepositoryUnitTest {

    @Autowired
    private CategoryRepositoryTestDatabasePrepator categoryTestDatabase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    public void prepareDatabase() {
        this.categoryTestDatabase.prepareTestDatabase();
    }


    // Test --- Find Category By Id

    @Test
    public void findCategoryByIdTest() {
        CategoryEntity category = this.arrangeSingleCategory();
        Response<List<CategoryEntity>> response =
                categoryRepository.findCategory(category.getCategoryId());
        this.validateFindCategoryResponse("Find Categories By Id",
                List.of(response, Response.success(List.of(category))));
    }

    // Test --- Find Category By Name & Id

    @Test
    public void findCategoryByNameAndIdTest() {
        CategoryEntity category = this.arrangeSingleCategory();
        Response<List<CategoryEntity>> response = categoryRepository
                .findCategory(category.getCategoryName(), category.getCategoryId());
        this.validateFindCategoryResponse("Find Categories By Name And Id",
                List.of(response, Response.success(List.of(category))));
    }

    // Test --- Categories By Name

    @Test
    public void findCategoriesByNameTest() {
        CategoryEntity category = this.arrangeSingleCategory();
        Response<List<CategoryEntity>> response =
                categoryRepository.findCategories(category.getCategoryName());
        this.validateFindCategoryResponse("Find Categories By Name",
                List.of(response, Response.success(List.of(category))));
    }

    // Section --- Test Arrangers

    public CategoryEntity arrangeSingleCategory() {
        /**
         * Create some test categories
         */
        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> category = query.from(CategoryEntity.class);
        query.select(category).where(criteria.equal(
                category.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                this.categoryTestDatabase.getPersistedCategories().get(3).getCategoryName()));

        return this.entityManager.createQuery(query).getSingleResult();
    }

    // Section --- Test Validators

    /**
     * validateFindCategoryResponse() tests if the responses match the expected response and fails
     * the test if they don't.
     * 
     * @param testName the test name for logs.
     * @param testResponses a list of responses to check, the first is marked as the expected one in
     *        logs.
     */
    public void validateFindCategoryResponse(String testName,
            List<Response<List<CategoryEntity>>> testResponses) {
        if (testResponses.size() != 2) {
            fail("Error: Bad Test Validator Call: Failed to Validate Test Responses: Response List too Small (<=1).");
            return;
        }

        Response<List<CategoryEntity>> expectedResponse = testResponses.get(0);
        Response<List<CategoryEntity>> currentResponse = testResponses.get(1);
        try {
            for (int i = 1; i < testResponses.size(); i++) {
                currentResponse = testResponses.get(i);
                assertEquals(expectedResponse, currentResponse);
            }
        } catch (Exception e) {
            String testDomain = String.format("Test Validator (%s)", testName);
            DebugLogger.displayEntity("Recieved Response", currentResponse, testDomain,
                    "Category Repository (Unit Test)");
            DebugLogger.displayEntity("Expected Response", expectedResponse, testDomain,
                    "Category Repository (Unit Test)");
            fail("Test Failed: Response format conflict. Expected: Success Response. Recieved: Failed Response.");
        }
    }
}
