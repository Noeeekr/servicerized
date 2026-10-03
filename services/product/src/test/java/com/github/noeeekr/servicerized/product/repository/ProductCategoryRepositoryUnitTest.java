package com.github.noeeekr.servicerized.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.product.repository.databases.CategoryRepositoryTestDatabase;
import com.github.noeeekr.servicerized.product.repository.databases.CategoryRepositoryTestDatabaseFactory;
import com.github.noeeekr.servicerized.product.repository.databases.ProductCategoryRepositoryTestDatabase;
import com.github.noeeekr.servicerized.product.repository.databases.ProductCategoryRepositoryTestDatabaseFactory;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabase;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabaseFactory;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryKey;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles({"in-memory-db"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProductCategoryRepositoryUnitTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    private ProductCategoryRepositoryTestDatabase productCategoryTestDatabase;
    private CategoryRepositoryTestDatabase categoryTestDatabase;
    private ProductRepositoryTestDatabase productTestDatabase;

    @Autowired
    public ProductCategoryRepositoryUnitTest(
            ProductCategoryRepositoryTestDatabaseFactory productCategoryTestDatabaseFactory,
            CategoryRepositoryTestDatabaseFactory categoryTestDatabaseFactory,
            ProductRepositoryTestDatabaseFactory productTestDatabaseFactory) {
        this.productTestDatabase = productTestDatabaseFactory.New();
        this.categoryTestDatabase = categoryTestDatabaseFactory.New();
        this.productCategoryTestDatabase = productCategoryTestDatabaseFactory.New(() -> {
            return List.of();
        });
    }

    @BeforeAll
    public void prepareDatabase() {
        transactionTemplate.executeWithoutResult((status) -> {
            this.productTestDatabase.prepareTestDatabase();
            this.categoryTestDatabase.prepareTestDatabase();
            this.productCategoryTestDatabase
                    .prepareTestDatabase(ProductCategoryRepositoryTestDatabase
                            .getRelations(this.productTestDatabase, this.categoryTestDatabase));
        });
    }

    @Test
    public void testPersistProductCategoryRelationSuccess() {
        ProductCategoryEntity expectedRelation = this.arrangeLocalProductCategoryRelation();
        ProductCategoryEntity recievedRelation;

        try {
            recievedRelation =
                    this.productCategoryRepository.save(Objects.requireNonNull(expectedRelation));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        this.validatePersistProductCategoryRelation(
                "Persist Product Category Relation (Success Test)", expectedRelation,
                recievedRelation);
    }

    @Test
    public void testFindRelationByProductAndCategoryIdSuccess() {
        ProductCategoryEntity expectedRelation =
                this.productCategoryTestDatabase.getPersistedRelations().get(1);
        ProductCategoryKey key = expectedRelation.getProductCategoryKey();

        ProductCategoryEntity recievedRelation = this.productCategoryRepository
                .findRelation(key.getProductId(), key.getCategoryId(), Limit.of(1));

        this.validateFindRelation("Find Relation By Product And Category Id (Success Test)",
                expectedRelation, recievedRelation);
    }

    @Test
    public void testFindRelationByProductAndCategoryNameSuccess() {
        ProductCategoryEntity expectedRelation =
                this.productCategoryTestDatabase.getPersistedRelations().get(1);
        ProductCategoryKey key = expectedRelation.getProductCategoryKey();

        CategoryEntity category = this.categoryTestDatabase.findPersistedById(key.getCategoryId());
        Objects.requireNonNull(category, "Could not find a suitable category for test");

        ProductCategoryEntity recievedRelation = this.productCategoryRepository
                .findRelation(key.getProductId(), category.getCategoryName(), Limit.of(1));

        this.validateFindRelation("Find Relation By Product And Category Name (Success Test)",
                expectedRelation, recievedRelation);
    }

    public ProductCategoryEntity arrangeLocalProductCategoryRelation() {
        List<ProductEntity> products = this.productTestDatabase.getPersistedProducts();
        ProductEntity product = products.get((products.size() / 2) + 1);

        CategoryEntity category = this.categoryTestDatabase.getPersistedCategories().get(3);

        return new ProductCategoryEntity(product, category);
    }

    public void validatePersistProductCategoryRelation(String testName,
            ProductCategoryEntity expectedRelation, ProductCategoryEntity recievedRelation) {
        if (recievedRelation == null) {
            fail("Test Failed: Product Category Relation Not Persisted.");
            return;
        }

        ProductCategoryKey recievedKey = recievedRelation.getProductCategoryKey();
        ProductCategoryKey expectedKey = expectedRelation.getProductCategoryKey();
        if (recievedKey == null) {
            fail("Test Failed: Product Category Relation Missing Composite Key.");
            return;
        }

        assertEquals(expectedKey.getProductId(), recievedKey.getProductId());
        assertEquals(expectedKey.getCategoryId(), recievedKey.getCategoryId());
    }

    public void validateFindRelation(String testName, ProductCategoryEntity expectedRelation,
            ProductCategoryEntity recievedRelation) {
        if (expectedRelation == null) {
            fail("Test Failed: Missing Expected Product Category Relation.");
            return;
        }

        if (recievedRelation == null) {
            fail("Test Failed: Product Category Relation Not Found In Database.");
            return;
        }

        ProductCategoryKey expectedKey = expectedRelation.getProductCategoryKey();
        ProductCategoryKey recievedKey = recievedRelation.getProductCategoryKey();

        assertNotNull(expectedKey);
        assertNotNull(recievedKey);
        assertEquals(expectedKey.getProductId(), recievedKey.getProductId());
        assertEquals(expectedKey.getCategoryId(), recievedKey.getCategoryId());
    }
}
