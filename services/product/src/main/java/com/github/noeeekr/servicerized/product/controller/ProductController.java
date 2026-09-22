package com.github.noeeekr.servicerized.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.noeeekr.servicerized.authorization.Authorization;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationCookieService;
import com.github.noeeekr.servicerized.authorization.cookie.AuthorizationToken;
import com.github.noeeekr.servicerized.controller.Controller;
import com.github.noeeekr.servicerized.product.failures.Failures;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductRequestInterface;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.service.ProductService;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.client.ClientResponse;

@RestController
@RequestMapping("/api/product")
public class ProductController extends Controller {
    @Autowired
    private ProductService productService;

    @Autowired
    private AuthorizationCookieService authorizationCookieService;

    @PostMapping()
    public ResponseEntity<ClientResponse> createProduct(
            @CookieValue(Authorization.AUTH_COOKIE_NAME) String authorizationCookie,
            @RequestBody CreateProductRequestInterface request) {
        AuthorizationToken token = authorizationCookieService.getPayload(authorizationCookie);
        if (authorizationCookieService.isTokenExpired(authorizationCookie)) {
            return this.handleFailure(
                    new Failures.AuthorizationFailed("Cookie de autorização não encontrado. ",
                            true),
                    ProductController.class.getCanonicalName(), "Create Product (Method)");
        }

        CreateProductInterface requestedProduct =
                CreateProductInterface.fromUpgrade(request, token.userId());

        Response<ProductEntity> createProductResponse =
                productService.createProduct(requestedProduct);

        if (createProductResponse.isSuccess() == false) {
            return this.handleFailure(createProductResponse.getFailure(),
                    ProductController.class.getCanonicalName(), "Create Product (Method)");
        }

        return new ResponseEntity<ClientResponse>(HttpStatus.NOT_IMPLEMENTED);
    }


}
