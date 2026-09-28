package com.github.noeeekr.servicerized.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.controller.Controller;
import com.github.noeeekr.servicerized.product.controller.models.request.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.controller.models.request.list.ListProductRequest;
import com.github.noeeekr.servicerized.product.failures.Failures;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.service.authorization.AuthorizationService;
import com.github.noeeekr.servicerized.product.service.product.ProductService;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateProductCommandInterface;
import com.github.noeeekr.servicerized.product.service.product.models.command.ListProductCommandInterface;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.client.ClientResponse;
import com.github.noeeekr.servicerized.response.client.ClientResponseDto;

@RestController
@RequestMapping("/api/product")
public class ProductController extends Controller {
        @Autowired
        private ProductService productService;

        @Autowired
        private AuthorizationService authorizationService;

        @PostMapping("/list")
        public ResponseEntity<ClientResponse> listProduct(
                        @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
                        @RequestBody ListProductRequest request) {
                /**
                 * Handle authorization through common authorization package.
                 */
                if (authorizationService.isTokenExpired(authorizationCookie)) {
                        return this.handleFailure(new Failures.AuthorizationFailed(
                                        "Cookie de autorização não encontrado. ", true),
                                        ProductController.class.getCanonicalName(),
                                        "Create Product (Method)");
                }

                /**
                 * Build a 'list product' instance by upgrading simple JSON to complex java
                 * structures.
                 */
                ListProductCommandInterface requestedProduct =
                                ListProductCommandInterface.upgrade(request);

                Response<List<ProductEntity>> listProductResponse =
                                productService.listProducts(requestedProduct);
                if (listProductResponse.isSuccess() == false)
                        return this.handleFailure(listProductResponse.getFailure(),
                                        "Product (Controller)", "List Product (Endpoint)");

                /**
                 * Execute the target operation
                 */
                List<ProductEntity> product = listProductResponse.getPayload();
                if (product.isEmpty()) {
                        ClientResponse responseBody =
                                        new ClientResponse(ClientResponseDto.EmptyPayload);
                        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
                }
                return ResponseEntity.ok(new ClientResponse(product));
        }

        @PostMapping()
        public ResponseEntity<ClientResponse> createProduct(
                        @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
                        @RequestBody CreateProductRequestInterface request) {
                /**
                 * Handle authorization through common authorization package.
                 */
                AuthorizationToken token = authorizationService.getPayload(authorizationCookie);
                if (authorizationService.isTokenExpired(authorizationCookie)) {
                        return this.handleFailure(new Failures.AuthorizationFailed(
                                        "Cookie de autorização não encontrado. ", true),
                                        ProductController.class.getCanonicalName(),
                                        "Create Product (Method)");
                }

                /**
                 * Build a 'create product' instance merging data from client with Api-populated
                 * fields.
                 */
                CreateProductCommandInterface requestedProduct =
                                CreateProductCommandInterface.upgrade(request, token.userId());

                /**
                 * Execute the target operation.
                 */
                Response<ProductEntity> createProductResponse =
                                productService.createProduct(requestedProduct);

                /**
                 * Handle the response
                 */
                if (createProductResponse.isSuccess() == false) {
                        return this.handleFailure(createProductResponse.getFailure(),
                                        ProductController.class.getCanonicalName(),
                                        "Create Product (Method)");
                }

                ClientResponse responseBody =
                                new ClientResponse(createProductResponse.getPayload());
                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }


}
