package com.github.noeeekr.servicerized.product.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.controller.Controller;
import com.github.noeeekr.servicerized.product.failures.Failures;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.service.authorization.AuthorizationService;
import com.github.noeeekr.servicerized.product.service.category.CategoryService;
import com.github.noeeekr.servicerized.product.service.category.ProductCategoryService;
import com.github.noeeekr.servicerized.product.service.category.models.command.AttachCategoryCommand;
import com.github.noeeekr.servicerized.product.service.category.models.command.DetachCategoryCommand;
import com.github.noeeekr.servicerized.product.service.category.models.command.ListCategoryCommand;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.client.ClientResponse;

@RestController
@RequestMapping("/api")
public class CategoriesController extends Controller {
        @Autowired
        private ProductCategoryService productCategoryService;

        @Autowired
        private AuthorizationService authorizationService;

        @Autowired
        private CategoryService categoryService;

        @GetMapping("/product/category")
        public ResponseEntity<ClientResponse> listCategories(
                        @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie) {
                /**
                 * Authorization: Requires user to contain credentials that are not expired.
                 */
                if (authorizationService.isTokenExpired(authorizationCookie)) {
                        return this.handleFailure(new Failures.AuthorizationFailed(
                                        "Falha de autorização: Credenciais não encontradas.", true),
                                        CategoriesController.class.getCanonicalName(),
                                        "List Categories (Endpoint)", "Authorization Service");
                } ;

                Response<List<CategoryEntity>> listCategoriesResponse = categoryService
                                .listCategories(new ListCategoryCommand(null, null, 50));
                if (!listCategoriesResponse.isSuccess())
                        this.handleFailure(listCategoriesResponse.getFailure(),
                                        "Category (Controller)", "List Categories (Endpoint)");
                List<CategoryEntity> categories = listCategoriesResponse.getPayload();

                return ResponseEntity.ok(new ClientResponse(categories));
        }

        @PutMapping("/product/{productId}/category/{categoryId}")
        public ResponseEntity<ClientResponse> attachCategory(
                        @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
                        @RequestParam("categoryId") UUID categoryId,
                        @RequestParam("productId") UUID productId) {
                /**
                 * Authorization: Requires user to contain credentials that are not expired.
                 */
                if (authorizationService.isTokenExpired(authorizationCookie)) {
                        return this.handleFailure(new Failures.AuthorizationFailed(
                                        "Falha de autorização: Credenciais não encontradas.", true),
                                        CategoriesController.class.getCanonicalName(),
                                        "Attach Category (Endpoint)", "Authorization Service");
                } ;

                /**
                 * Preparation: Recover user data & create request.
                 */
                AuthorizationToken authorizationToken =
                                authorizationService.getPayload(authorizationCookie);
                AttachCategoryCommand attachCategoryRequest = new AttachCategoryCommand(
                                authorizationToken.userId(), productId, categoryId);

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
                                        CategoriesController.class.getCanonicalName(),
                                        "Attach Category (Endpoint)", "Categories Service");
                }

                return new ResponseEntity<>(new ClientResponse(attachCategoryResponse.getPayload()),
                                HttpStatus.CREATED);
        }

        @DeleteMapping("/product/{productId}/category/{categoryId}")
        public ResponseEntity<ClientResponse> dettachCategory(
                        @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
                        @RequestParam("categoryId") UUID categoryId,
                        @RequestParam("productId") UUID productId) {
                /**
                 * Authorization: Requires user to contain credentials that are not expired.
                 */
                if (authorizationService.isTokenExpired(authorizationCookie)) {
                        return this.handleFailure(new Failures.AuthorizationFailed(
                                        "Falha de autorização: Credenciais não encontradas.", true),
                                        CategoriesController.class.getCanonicalName(),
                                        "Attach Category (Endpoint)", "Authorization Service");
                } ;

                /**
                 * Preparation: Recover user data & create request.
                 */
                AuthorizationToken authorizationToken =
                                authorizationService.getPayload(authorizationCookie);
                DetachCategoryCommand dettachCategoryRequest = new DetachCategoryCommand(
                                authorizationToken.userId(), productId, categoryId);

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
                                        CategoriesController.class.getCanonicalName(),
                                        "Dettach Category (Endpoint)", "Categories Service");
                }

                return new ResponseEntity<>(
                                new ClientResponse(dettachCategoryResponse.getPayload()),
                                HttpStatus.OK);
        }
}
