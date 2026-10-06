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
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabase;
import com.github.noeeekr.servicerized.product.repository.databases.ProductRepositoryTestDatabaseFactory;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles({"in-memory-db"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class VirtualProductRepositoryUnitTest {
    public String getDomain() {
        return "Virtual Product Repository (Test Class)";
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private VirtualProductRepository virtualProductRepository;

    private ProductRepositoryTestDatabase productTestDatabase;

    @Autowired
    public VirtualProductRepositoryUnitTest(
            ProductRepositoryTestDatabaseFactory productRepositoryTestDatabaseFactory) {
        this.productTestDatabase = productRepositoryTestDatabaseFactory.New();
    }

    @BeforeAll
    public void prepareDatabase() {
        transactionTemplate.executeWithoutResult((status) -> {
            this.productTestDatabase.prepareTestDatabase();
        });
    }

    @Test
    public void testPersistVirtualProduct() {
        VirtualProductEntity virtualProduct = this.arrangeSingleLocalVirtualProduct();
        Response<VirtualProductEntity> recievedResponse;
        try {
            recievedResponse = virtualProductRepository.persist(virtualProduct);
        } catch (Throwable e) {
            DebugLogger.displayThrowable(e, "Persist Virtual Product (Test Method)",
                    "Test Target Failed Execution.");
            fail(e.getMessage());
            return;
        }
        this.validatePersistVirtualProduct("Persist Virtual Product (Test Method)",
                Response.success(virtualProduct), recievedResponse);
    }


    public VirtualProductEntity arrangeSingleLocalVirtualProduct() {
        ProductEntity product = this.productTestDatabase.getPersistedProducts().get(2);
        return new VirtualProductEntity(product, 6);
    }

    public void validatePersistVirtualProduct(String testName,
            Response<VirtualProductEntity> expectedResponse,
            Response<VirtualProductEntity> recievedResponse) {
        if (expectedResponse.isSuccess() != recievedResponse.isSuccess()) {
            String message = "Test Failure Detected: Response State Differ. ";
            this.failLogUnwantedResponse(testName, message, recievedResponse);
        }

        try {
            assertEquals(expectedResponse, recievedResponse);
        } catch (AssertionFailedError e) {
            DebugLogger.displayEntity("Recieved Entity", expectedResponse, this.getDomain(),
                    testName, "Persist Virtual Product (Test Validator)", "Validate Success");
            DebugLogger.displayEntity("Expected Entity", recievedResponse, this.getDomain(),
                    testName, "Persist Virtual Product (Test Validator)", "Validate Success");
        }
    }

    public void failLogUnwantedResponse(String testName, String message, Response<?> response) {
        if (response.isSuccess()) {
            this.logUnwantedSuccessResponse(testName, message, response);
        } else {
            this.logUnwantedFailedResponse(testName, message, response);
        }
    }

    public void logUnwantedSuccessResponse(String testName, String message, Response<?> response) {
        DebugLogger.displayEntity("Recieved Entity", response, this.getDomain(), testName,
                "Persist Virtual Product (Test Validator)", "Validate Differ (Branch)");
        fail(message
                + "Expected Failure, Recieved Success Instead. More Information Available In Debug Logs.");
    }

    public void logUnwantedFailedResponse(String testName, String message, Response<?> response) {
        DebugLogger.displayFailure(response.getFailure(), this.getDomain(), testName,
                "Persist Virtual Product (Test Validator)", "Validate Differ (Branch)");
        fail(message
                + "Expected Failure, Recieved Success Instead. More Information Available In Debug Logs.");
    }
}
