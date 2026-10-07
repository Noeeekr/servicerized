package com.github.noeeekr.servicerized.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabase;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabaseFactory;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductKindEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.transaction.Transactional;

@Transactional 
@SpringBootTest
@ActiveProfiles({"in-memory-db"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProductKindRepositoryUnitTest {

    // Section --- Transaction handlers
    @Autowired
    private TransactionTemplate transactionTemplate;

    // Section --- Repositories

    @Autowired
    private ProductKindRepository productKindRepository;
    @Autowired
    private KindRepository kindRepository;

    // Section --- Test databases

    private ProductRepositoryTestDatabase productTestDatabase;

    // Section --- Constructors

    @Autowired
    public ProductKindRepositoryUnitTest(
            ProductRepositoryTestDatabaseFactory productDatabaseTestFactory) {
        this.productTestDatabase = productDatabaseTestFactory.New();
    }

    // Section --- Test Preparation

    @BeforeAll
    public void prepareDatabase() {
        transactionTemplate.executeWithoutResult((status) -> {
            this.productTestDatabase.prepareTestDatabase();
        });
    }

    // Test --- Persist Product Kind Relation success

    @Test
    public void testPersistRelation() {
        ProductKindEntity relation = this.arrangeSingleRelation();
        Response<ProductKindEntity> recievedResponse;
        try {
            recievedResponse = productKindRepository.persist(relation);
        } catch (Throwable e) {
            DebugLogger.displayThrowable(e, "Persist Product Kind Relation (Test Method)",
                    "Test Target Failed Execution.");
            fail(e.getMessage());
            return;
        }
        this.validatePersistRelationProduct("Persist Product Kind Relation (Test Method)",
                Response.success(relation), recievedResponse);
    }

    // Section --- Test data arrangers

    public ProductKindEntity arrangeSingleRelation() {
        ProductEntity product = this.productTestDatabase.getPersistedProducts().get(2);
        KindEntity kind = this.kindRepository.getReferenceManager().getVirtualServiceKind();
        return new ProductKindEntity(product, kind);
    }

    // Section --- Test result validators

    public void validatePersistRelationProduct(String testName,
            Response<ProductKindEntity> expectedResponse,
            Response<ProductKindEntity> recievedResponse) {
        if (expectedResponse.isSuccess() != recievedResponse.isSuccess()) {
            String message = "Test Failure Detected: Response State Differ. ";
            this.failLogUnwantedResponse(testName, message, expectedResponse.isSuccess(), recievedResponse);
        }

        try {
            assertEquals(expectedResponse, recievedResponse);
        } catch (AssertionFailedError e) {
            DebugLogger.displayEntity("Recieved Entity", expectedResponse, this.getDomain(),
                    testName, "Persist Product Kind Relation (Test Validator)", "Validate Success");
            DebugLogger.displayEntity("Expected Entity", recievedResponse, this.getDomain(),
                    testName, "Persist Product Kind Relation (Test Validator)", "Validate Success");
        }
    }

    // Section --- Result logging

    public void failLogUnwantedResponse(String testName, String message, boolean success, Response<?> response) {
        if (!success && response.isSuccess()) {
            this.logUnwantedSuccessResponse(testName, message, response);
        } else {
            this.logUnwantedFailedResponse(testName, message, response);
        }
    }

    public void logUnwantedSuccessResponse(String testName, String message, Response<?> response) {
        DebugLogger.displayEntity("Recieved Entity", response, this.getDomain(), testName,
                "Persist Product Kind Relation (Test Validator)", "Validate Differ (Branch)");
        fail(message
                + "Expected Failure, Recieved Success Instead. More Information Available In Debug Logs.");
    }

    public void logUnwantedFailedResponse(String testName, String message, Response<?> response) {
        DebugLogger.displayFailure(response.getFailure(), this.getDomain(), testName,
                "Persist Product Kind Relation (Test Validator)", "Validate Differ (Branch)");
        fail(message
                + "Expected Success, Recieved Failure Instead. More Information Available In Debug Logs.");
    }

    // Section --- Others

    public final String getDomain() {
        return "Product Kind Relation Repository (Test Class)";
    }
}
