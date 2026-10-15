package com.github.noeeekr.servicerized.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateVirtualProductRequest;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.service.authorization.AuthorizationService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("in-memory-db")
@Transactional
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private EntityManager entityManager;

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

    private Cookie authorizationCookie(UUID userId) throws Exception {
        String token = authorizationService.createToken(userId);
        AuthorizationToken payload = authorizationService.getPayload(token);
        assertEquals(userId, payload.userId());
        return new Cookie(Authorization.AUTH_COOKIE_NAME, token);
    }
}
