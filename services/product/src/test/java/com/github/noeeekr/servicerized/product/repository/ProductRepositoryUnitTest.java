package com.github.noeeekr.servicerized.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Objects;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.noeeekr.servicerized.logging.DebugLogger;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"in-memory-db"})
public class ProductRepositoryUnitTest {
    public final String getDomain() {
        return "Product Repository (Unit Test)";
    }

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepositoryTestDatabase productTestDatabase;


    // Test --- List Product

    @Test
    public void testListRandomProductSuccess() {
        Response<ProductEntity> response = productRepository.listProduct();
        this.validateListRandomProductSuccess("List Random (Success Test)", response);
    }

    @Test
    public void testListProductByIdSuccess() {
        ProductEntity product = this.arrangePersistedProduct();
        System.out.println(product.getProductId());
        Response<ProductEntity> response = productRepository.listProduct(product.getProductId());
        this.validateListProduct("List By Id (Success Test)", Response.success(product), response);
    }

    @Test
    public void testListProductByIdAndOwnerIdSuccess() {
        ProductEntity product = this.arrangePersistedProduct();
        System.out.println(product.getProductId());
        Response<ProductEntity> response =
                productRepository.listProduct(product.getProductId(), product.getOwnerId());
        this.validateListProduct("List By Id And Owner Id (Success Test)",
                Response.success(product), response);
    }

    // @Test
    // public void testListProductByIdAndCategoriesSuccess() {
    // ProductEntity product = this.arrangePersistedProduct();
    // System.out.println(product.getProductId());

    // ProductFilterableFieldsInterface productFilter = new ProductFilter(product);
    // List<CategoryFilterableFieldsInterface> productCategoryFilter =
    // List.of(new CategoryFilter());

    // Response<ProductEntity> response =
    // productRepository.listProduct(product.getProductId(), null);
    // this.validateListProduct("List By Id And Owner Id (Success Test)",
    // Response.success(product), response);
    // }

    // Test --- Persist Product

    /**
     * testPersistSuccess() tests if a single product can be persisted correctly into database.
     * 
     * For other test safety, after inserting the product it removes the product from the local list
     * and insert it into the persisted list. Additionally, for the same reason, persist and
     * mutation tests are meant to be ran after list tests.
     */
    @Test()
    @Order(1000)
    public void testPersistSuccess() {
        ProductEntity product = this.productTestDatabase.getLocalProducts().get(3);
        Response<ProductEntity> response = productRepository.persist(product);
        this.validatePersist("Persist Success (Test)", Response.success(product), response);
    }

    // Section --- Test Arrangers

    public ProductEntity arrangePersistedProduct() {
        /**
         * Fetch a single test product
         */
        CriteriaBuilder criteria = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = criteria.createQuery(ProductEntity.class);
        Root<ProductEntity> product = query.from(ProductEntity.class);
        query.select(product)
                .where(criteria.equal(product.get(ProductEntity.METADATA.COLUMN_NAME_PRODUCT_NAME),
                        this.productTestDatabase.getPersistedProducts().get(3).getProductName()));

        return this.entityManager.createQuery(query).getSingleResult();
    };

    // Section --- Test Validators

    /**
     * validatePersist() checks if a response recieved by calling ProductRepository.listProduct()
     * for a random product is a valid product and matches agaisn't any of the predefined persisted
     * products.
     * 
     * @param testName The name of the test for debugging purposes.
     * @param recievedProduct The response of the listProduct().
     * 
     * @throws AssertionFailedError
     */
    public void validateListRandomProductSuccess(String testName,
            Response<ProductEntity> recievedResponse) throws AssertionFailedError {
        if (!recievedResponse.isSuccess()) {
            if (recievedResponse.getFailure().error() == null) {
                fail("Response Failure Detected: No Exceptions were Thrown.");
            }

            DebugLogger.displayThrowable(recievedResponse.getFailure().error(), this.getDomain(),
                    testName, "Validate List Random Success", "Response Failure Exception");
            fail("Response Failure Detected: Exception Available in Debug Logs.");
        }

        boolean found = this.productTestDatabase.getPersistedProducts()
                .contains(recievedResponse.getPayload());
        if (!found) {
            DebugLogger.displayThrowable(recievedResponse.getFailure().error(), this.getDomain(),
                    testName, "Validate List Random Success", "Listed Product Not Found");
            fail("Product Not Found: Product Does Not Exist In Expected Results.");
        }
    }

    /**
     * validatePersist() checks if a response recieved by ProductRepository.listProduct() for a
     * delimited product matches agaisn't the expected response.
     * 
     * @param testName The name of the test for debugging purposes.
     * @param expectedResponse The expected response of the listProduct() that is used for
     *        comparison.
     * @param recievedResponse The response to compare agaisn't the expected response.
     * 
     * @throws AssertionFailedError
     */
    public void validateListProduct(String testName, Response<ProductEntity> expectedResponse,
            Response<ProductEntity> recievedResponse) throws AssertionFailedError {
        if (expectedResponse.isSuccess() != recievedResponse.isSuccess()) {
            DebugLogger.displayFailure(recievedResponse.getFailure(), this.getDomain(), testName,
                    "List Product (Validator)", "Failure Exception");
            fail("Test Failed: Response Sucess State Differ. More Information Available in Debug Logs.");
        }

        if (expectedResponse.isSuccess()) {
            this.validateListProductSuccess(testName, expectedResponse, recievedResponse);
        } else {
            this.validateListProductFailure(testName, expectedResponse, recievedResponse);
        }
    }

    public void validateListProductSuccess(String testName,
            Response<ProductEntity> expectedResponse, Response<ProductEntity> recievedResponse)
            throws AssertionFailedError {
        assertEquals(expectedResponse, recievedResponse);
    }

    public void validateListProductFailure(String testName,
            Response<ProductEntity> expectedResponse, Response<ProductEntity> recievedResponse)
            throws AssertionFailedError {
        try {
            assertEquals(expectedResponse, recievedResponse);
        } catch (Exception e) {
            DebugLogger.displayThrowable(recievedResponse.getFailure().error(), this.getDomain(),
                    testName, "List Product Failure (Validator)", "Failure Exception");
        }
    }

    /**
     * validatePersist() checks if a response recieved by ProductRepository.persist() matches the
     * expected response. Since the database is expected to create fields, those are checked by
     * existence and not by comparison, which also means it is not needed to "guess" them in the
     * expected response.
     * 
     * @param testName The name of the test for debugging purposes.
     * @param expectedResponse The correct response that is used for comparison.
     * @param recievedResponse A response to check.
     * 
     * @throws AssertionFailedError
     */
    public void validatePersist(String testName, Response<ProductEntity> expectedResponse,
            Response<ProductEntity> recievedResponse) throws AssertionFailedError {
        if (recievedResponse == null || expectedResponse == null) {
            fail("Error: Bad Test Validator Call: Failed to Validate Test Responses: Response List too Small (<=1).");
            return;
        }

        try {
            if (expectedResponse.isSuccess() != recievedResponse.isSuccess()) {
                fail("Test Failed: Response success state differ.");
            }

            this.validatePersistFailure(expectedResponse, recievedResponse);
            this.validatePersistSuccess(expectedResponse, recievedResponse);
        } catch (AssertionFailedError e) {
            if (!recievedResponse.isSuccess() && recievedResponse.getFailure().error() != null) {
                this.logPersistFailureByException(testName, recievedResponse);
            } else {
                this.logPersistFailureByResponseDiffer(testName, recievedResponse,
                        expectedResponse);
            }
        }
    }

    public void validatePersistFailure(Response<ProductEntity> expectedResponse,
            Response<ProductEntity> recievedResponse) throws AssertionFailedError {
        if (!expectedResponse.isSuccess()) {
            assertEquals(expectedResponse, recievedResponse);
        }
    }

    public void validatePersistSuccess(Response<ProductEntity> expectedResponse,
            Response<ProductEntity> recievedResponse) throws AssertionFailedError {
        // Reflect populated fields for assert compatibility.
        // Dont affect the test negatively as long exists, if don't then a test for population
        // handles the issue.

        ProductEntity expectedProduct = expectedResponse.getPayload();
        ProductEntity recievedProduct = recievedResponse.getPayload();

        expectedProduct = new ProductEntity(recievedProduct.getId(), expectedProduct.getOwnerId(),
                expectedProduct.getName(), expectedProduct.getPrice(),
                expectedProduct.getDescription());
        expectedProduct.setCreatedAt(recievedProduct.getCreatedAt());
        expectedProduct.setDeletedAt(recievedProduct.getDeletedAt());
        expectedProduct.setUpdatedAt(recievedProduct.getUpdatedAt());
        expectedResponse.replacePayload(expectedProduct);

        // Check if database populated fields correctly.

        if (Objects.equals(null, recievedProduct.getId())) {
            fail("Test Failed: Response Product has Null Id.");
        } ;

        assertEquals(expectedResponse, recievedResponse);
    }

    // Section -- Debug Logging

    public void logPersistFailureByException(String testName,
            Response<ProductEntity> recievedResponse) {
        DebugLogger.displayThrowable(recievedResponse.getFailure().error(), this.getDomain(),
                testName, "Recieved Response Failure");
        fail("Test Failed: Response format conflict. Expected: Success Response. Recieved: Failed Response.");
    }

    public void logPersistFailureByResponseDiffer(String testName,
            Response<ProductEntity> recievedResponse, Response<ProductEntity> expectedResponse) {
        DebugLogger.displayEntity("Recieved Response Payload", recievedResponse.getPayload(),
                testName, this.getDomain());
        DebugLogger.displayEntity("Expected Response Payload", expectedResponse.getPayload(),
                testName, this.getDomain());
        fail("Test Failed: Response format conflict. Responses are different.");
    }
}
