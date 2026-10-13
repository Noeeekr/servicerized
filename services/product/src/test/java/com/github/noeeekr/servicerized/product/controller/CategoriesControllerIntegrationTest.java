package com.github.noeeekr.servicerized.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.service.authorization.AuthorizationService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("in-memory-db")
@Transactional
public class CategoriesControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private EntityManager entityManager;

    /**
     * Tests that the category endpoint returns persisted categories for a valid authorization
     * cookie.
     */
    @Test
    public void listCategoriesSuccess() throws Exception {
        UUID ownerId = UuidCreator.getTimeOrderedEpoch();

        mockMvc.perform(get("/api/product/category")
                .cookie(authorizationCookie(ownerId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Digital Art")));
    }

    /**
     * Tests that an existing product can be assigned an existing category.
     */
    @Test
    public void attachCategorySuccess() throws Exception {
        UUID ownerId = UuidCreator.getTimeOrderedEpoch();
        ProductEntity product = createProduct(ownerId, "Integration test attach product");
        CategoryEntity category =
                createCategory("Integration test attach category");

        mockMvc.perform(put("/api/product/{productId}/category/{categoryId}",
                product.getProductId(), category.getCategoryId())
                .queryParam("productId", product.getProductId().toString())
                .queryParam("categoryId", category.getCategoryId().toString())
                .cookie(authorizationCookie(ownerId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Tests that an existing product-category assignment can be removed.
     */
    @Test
    public void detachCategorySuccess() throws Exception {
        UUID ownerId = UuidCreator.getTimeOrderedEpoch();
        ProductEntity product = createProduct(ownerId, "Integration test detach product");
        CategoryEntity category =
                createCategory("Integration test detach category");
        entityManager.persist(new ProductCategoryEntity(product, category));
        entityManager.flush();

        mockMvc.perform(delete("/api/product/{productId}/category/{categoryId}",
                product.getProductId(), category.getCategoryId())
                .queryParam("productId", product.getProductId().toString())
                .queryParam("categoryId", category.getCategoryId().toString())
                .cookie(authorizationCookie(ownerId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private ProductEntity createProduct(UUID ownerId, String name) {
        ProductEntity product = ProductEntity.builder()
                .ownerId(ownerId)
                .name(name)
                .price(90)
                .description("Prepared for category integration test")
                .build();
        entityManager.persist(product);
        entityManager.flush();
        return product;
    }

    private CategoryEntity createCategory(String name) {
        CategoryEntity category = new CategoryEntity(null, name);
        entityManager.persist(category);
        entityManager.flush();
        return category;
    }

    private Cookie authorizationCookie(UUID userId) throws Exception {
        String token = authorizationService.createToken(userId);
        AuthorizationToken payload = authorizationService.getPayload(token);
        assertEquals(userId, payload.userId());
        return new Cookie(Authorization.AUTH_COOKIE_NAME, token);
    }
}
