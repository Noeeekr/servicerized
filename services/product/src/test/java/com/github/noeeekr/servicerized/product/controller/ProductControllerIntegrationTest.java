package com.github.noeeekr.servicerized.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.product.controller.ProductController.METADATA.ENDPOINT;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateVirtualProductRequest;
import com.github.noeeekr.servicerized.product.controller.models.request.UpdateVirtualProductInformationRequest;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.query.update.UpdateProductFields;
import com.github.noeeekr.servicerized.product.repository.models.query.update.UpdateVirtualProductFields;
import com.github.noeeekr.servicerized.product.repository.models.relations.VirtualProductInformation;
import com.github.noeeekr.servicerized.product.service.ProductServiceTest;
import com.github.noeeekr.servicerized.product.service.authorization.AuthorizationService;
import com.github.noeeekr.servicerized.product.service.product.ProductService;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("in-memory-db")
public class ProductControllerIntegrationTest {
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    /**
     * ExpectationConfigurator defines a callback function that is expected to configure expected
     * test result.
     */
    @FunctionalInterface
    private interface ExpectationConfigurator {
        public ResultActions configure(ResultActions action) throws Exception;
    }

    /**
     * Tests that a valid virtual product creation request is persisted and returns HTTP 201.
     */
    @Test
    public void createProductSuccess() throws Exception {
        UUID ownerId = UuidCreator.getTimeOrderedEpoch();

        CreateVirtualProductRequest request = new CreateVirtualProductRequest();
        request.setProductName("Integration test virtual product");
        request.setProductPrice(125);
        request.setProductDescription("Created through the product endpoint");
        request.setProvisionHours(3);

        String content = Objects.requireNonNull(objectMapper.writeValueAsString(request));
        MediaType contentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);

        MediaType expectedContentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);

        mockMvc.perform(post("/api/product").cookie(authorizationCookie(ownerId)).content(content)
                .accept(MediaType.APPLICATION_JSON).contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(expectedContentType))
                .andExpect(jsonPath("$.payload.productName")
                        .value("Integration test virtual product"));
    }

    /**
     * Tests that a valid list request returns products already associated with a category.
     */
    @Test
    public void listProductSuccess() throws Exception {
        UUID ownerId = UuidCreator.getTimeOrderedEpoch();
        ProductEntity product =
                ProductEntity.builder().ownerId(ownerId).name("Integration test listed product")
                        .price(75).description("Product returned by the list endpoint").build();
        CategoryEntity category = new CategoryEntity(null, "Integration test listed category");
        entityManager.persist(product);
        entityManager.persist(category);
        entityManager.flush();
        entityManager.persist(new ProductCategoryEntity(product, category));
        entityManager.flush();

        MediaType contentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);
        MediaType expectedContentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);

        mockMvc.perform(post("/api/product/list").cookie(authorizationCookie(ownerId))
                .content("{\"productFilter\":{\"productId\":null,"
                        + "\"productName\":\"Integration test listed product\"},"
                        + "\"categoryFilter\":[{\"categoryId\":null,"
                        + "\"categoryName\":\"Integration test listed category\"}]}")
                .accept(MediaType.APPLICATION_JSON).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().contentType(expectedContentType))
                .andExpect(jsonPath("$.payload[0].productName")
                        .value("Integration test listed product"));
    }

    @Test
    public void updateProductSuccess() throws Exception {
        List<UpdateProductTestcase> testCases = this.arrangeUpdateVirtualProductSuccess();

        for (int i = 0; i < testCases.size(); i++) {
            UpdateProductTestcase testCase = testCases.get(i);

            UpdateVirtualProductInformationRequest request = testCase.getUpdateRequest();

            MediaType contentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);
            UUID ownerId = request.getProductFilter().getProductOwnerId();
            String json = Objects.requireNonNull(objectMapper.writeValueAsString(request));

            String endpoint = ENDPOINT.ROOT + ENDPOINT.UPDATE_OWNED_VIRTUAL_PRODUCT;

            ResultActions resultActions =
                    this.mockMvc.perform(patch(endpoint).cookie(authorizationCookie(ownerId))
                            .accept(contentType).contentType(contentType).content(json));
            testCase.configureExpectation(resultActions);
        }
    }

    // Section --- Cookie Helpers
    private Cookie authorizationCookie(UUID userId) throws Exception {
        String token = authorizationService.createToken(userId);
        AuthorizationToken payload = authorizationService.getPayload(token);
        assertEquals(userId, payload.userId());
        return new Cookie(Authorization.AUTH_COOKIE_NAME, token);
    }


    // Section --- test cases

    private class UpdateProductTestcase {
        private UpdateVirtualProductInformationRequest updateRequest;

        private ExpectationConfigurator expectationConfigurator;

        public UpdateProductTestcase(UpdateVirtualProductInformationRequest body,
                ExpectationConfigurator expectationConfigurator) {
            this.expectationConfigurator = expectationConfigurator;
            this.updateRequest = body;
        }

        public ResultActions configureExpectation(ResultActions action) throws Exception {
            return expectationConfigurator.configure(action);
        }

        public UpdateVirtualProductInformationRequest getUpdateRequest() {
            return this.updateRequest;
        }
    }

    // Section --- Test Case Creators

    private UpdateProductTestcase createUpdateVirtualProductSuccessTestCase(
            VirtualProductEntity virtualProduct) {
        UpdateVirtualProductInformationRequest request =
                createUpdateVirtualProductRequest(virtualProduct);
        return new UpdateProductTestcase(request, (action) -> {
            MediaType expectedContentType = Objects.requireNonNull(MediaType.APPLICATION_JSON);

            return action.andExpect(status().isOk())
                    .andExpect(content().contentType(expectedContentType))
                    .andExpect(jsonPath("$.payload.updatedAmount").value(1));
        });
    }

    // Section --- Test Data Arrangers

    private List<UpdateProductTestcase> arrangeUpdateVirtualProductSuccess() {
        List<VirtualProductEntity> virtualProducts = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Response<VirtualProductInformation> response =
                    ProductServiceTest.Mock.createVirtualProduct(productService);
            if (!response.isSuccess()) {
                FailIfBadResponse(response, "database prepopulation failed");
            }
            virtualProducts.add(response.getPayload().getVirtualProduct());
        }

        List<UpdateProductTestcase> testCases = new ArrayList<>();
        for (int i = 0; i < virtualProducts.size(); i++) {
            UpdateProductTestcase testCase =
                    createUpdateVirtualProductSuccessTestCase(virtualProducts.get(i));
            testCases.add(testCase);
        }

        return testCases;
    }

    // Section --- Request Creator Helpers

    private UpdateVirtualProductInformationRequest createUpdateVirtualProductRequest(
            VirtualProductEntity virtualProduct) {
        UpdateVirtualProductFields virtualProductFields = new UpdateVirtualProductFields(
                Integer.valueOf(virtualProduct.getProvisionHours() + 1),
                Integer.valueOf(virtualProduct.getMaxWeeklyProvisionHours() + 1));
        UpdateProductFields productFields =
                new UpdateProductFields(virtualProduct.getProduct().getName() + "-update",
                        virtualProduct.getProduct().getProductDescription() + "-update",
                        virtualProduct.getProduct().getProductPrice() + 10);
        ProductFilter filter = new ProductFilter(virtualProduct.getProductId(),
                virtualProduct.getProduct().getProductName());

        UpdateVirtualProductInformationRequest request = new UpdateVirtualProductInformationRequest(
                productFields, virtualProductFields, filter);

        return request;
    }

    // Section --- Fail Helpers

    public void FailIfBadResponse(Response<?> response, String message) {
        if (response.isSuccess()) {
            return;
        }
        if (message != null && !message.isBlank()) {
            message += ": ";
        }
        fail(message + response.getFailure().message());
    }
}
