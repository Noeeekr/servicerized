package com.github.noeeekr.servicerized.product.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationCookieService;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.controller.Controller;
import com.github.noeeekr.servicerized.product.failures.Failures;
import com.github.noeeekr.servicerized.product.repository.models.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.service.ProductCategoryService;
import com.github.noeeekr.servicerized.product.service.request.AttachCategoryRequest;
import com.github.noeeekr.servicerized.product.service.request.DetachCategoryRequest;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.client.ClientResponse;

@RestController
@RequestMapping("/api/product/{productId}/category/{categoryId}")
public class CategoriesController extends Controller {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private AuthorizationCookieService authorizationCookieService;

    @PutMapping()
    public ResponseEntity<ClientResponse> attachCategory(
            @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("productId") UUID productId) {
        /**
         * Authorization: Requires user to contain credentials that are not expired.
         */
        if (authorizationCookieService.isTokenExpired(authorizationCookie)) {
            return this.handleFailure(
                    new Failures.AuthorizationFailed(
                            "Falha de autorização: Credenciais não encontradas.", true),
                    CategoriesController.class.getCanonicalName(), "Attach Category (Endpoint)",
                    "Authorization Service");
        } ;

        /**
         * Preparation: Recover user data & create request.
         */
        AuthorizationToken authorizationToken =
                authorizationCookieService.getPayload(authorizationCookie);
        AttachCategoryRequest attachCategoryRequest =
                new AttachCategoryRequest(authorizationToken.userId(), productId, categoryId);

        /**
         * Execution: Trigger the target operations.
         */
        Response<ProductCategoryEntity> attachCategoryResponse =
                productCategoryService.attachCategory(attachCategoryRequest);

        /**
         * Error Handling
         */
        if (attachCategoryResponse.isSuccess() == false) {
            return this.handleFailure(attachCategoryResponse.getFailure(),
                    CategoriesController.class.getCanonicalName(), "Attach Category (Endpoint)",
                    "Categories Service");
        }

        return new ResponseEntity<>(new ClientResponse(attachCategoryResponse.getPayload()),
                HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<ClientResponse> dettachCategory(
            @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("productId") UUID productId) {
        /**
         * Authorization: Requires user to contain credentials that are not expired.
         */
        if (authorizationCookieService.isTokenExpired(authorizationCookie)) {
            return this.handleFailure(
                    new Failures.AuthorizationFailed(
                            "Falha de autorização: Credenciais não encontradas.", true),
                    CategoriesController.class.getCanonicalName(), "Attach Category (Endpoint)",
                    "Authorization Service");
        } ;

        /**
         * Preparation: Recover user data & create request.
         */
        AuthorizationToken authorizationToken =
                authorizationCookieService.getPayload(authorizationCookie);
        DetachCategoryRequest dettachCategoryRequest =
                new DetachCategoryRequest(authorizationToken.userId(), productId, categoryId);

        /**
         * Execution: Trigger the target operations.
         */
        Response<ProductCategoryEntity> dettachCategoryResponse =
                productCategoryService.dettachCategory(dettachCategoryRequest);

        /**
         * Error Handling
         */
        if (dettachCategoryResponse.isSuccess() == false) {
            return this.handleFailure(dettachCategoryResponse.getFailure(),
                    CategoriesController.class.getCanonicalName(), "Dettach Category (Endpoint)",
                    "Categories Service");
        }

        return new ResponseEntity<>(new ClientResponse(dettachCategoryResponse.getPayload()),
                HttpStatus.OK);
    }
}
