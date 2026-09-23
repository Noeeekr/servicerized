package com.github.noeeekr.servicerized.product.service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.failures.RepositoryFailures;
import com.github.noeeekr.servicerized.product.repository.ProductCategoryRepository;
import com.github.noeeekr.servicerized.product.repository.models.CategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.ProductCategoryKey;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.service.request.AttachCategoryRequest;
import com.github.noeeekr.servicerized.product.service.request.DetachCategoryRequest;
import com.github.noeeekr.servicerized.product.service.request.FindCategoryRequest;
import com.github.noeeekr.servicerized.product.service.request.FindOneProductRequest;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    public Response<ProductCategoryEntity> dettachCategory(DetachCategoryRequest detachRequest) {
        /**
         * Data Integrity: Validate product existence & ownership.
         */
        Response<Optional<ProductEntity>> findProductResponse = productService.findProduct(
                new FindOneProductRequest(detachRequest.productId(), detachRequest.userId()));
        if (findProductResponse.isSuccess() == false)
            return Response.fromFailure(findProductResponse);
        if (findProductResponse.getPayload().isEmpty())
            return Response.fromFailure(new RepositoryFailures.ResourceNotFound(
                    "Usuário não contém o produto especificado. "));

        /**
         * Data Integrity: Validate category existence.
         */
        Response<Optional<CategoryEntity>> findCategoryResponse = categoryService
                .findCategory(new FindCategoryRequest(detachRequest.categoryId(), null));
        if (findCategoryResponse.isSuccess() == false)
            return Response.fromFailure(findCategoryResponse);
        if (findCategoryResponse.getPayload().isEmpty())
            return Response.fromFailure(new RepositoryFailures.ResourceNotFound(
                    "A categoria deseja não existe no sistema. "));

        /**
         * Create entity to be persisted.
         */
        ProductCategoryEntity entity = new ProductCategoryEntity(
                new ProductCategoryKey(detachRequest.productId(), detachRequest.categoryId()));

        /**
         * Execute persist operation & Handle exceptions.
         */
        try {
            entity = productCategoryRepository.findRelation(detachRequest.productId(),
                    detachRequest.categoryId(), Limit.of(1));
        } catch (Exception e) {
            return Response.<ProductCategoryEntity>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        }

        if (entity == null)
            return Response.fromFailure(new RepositoryFailures.ResourceNotFound(
                    "A categoria não está atribuída ao produto."));

        try {
            productCategoryRepository.delete(entity);
        } catch (Exception e) {
            return Response.<ProductCategoryEntity>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        }

        return Response.success(entity);
    }

    public Response<ProductCategoryEntity> attachCategory(AttachCategoryRequest attachRequest) {
        /**
         * Data Integrity: Validate product existence & ownership.
         */
        Response<Optional<ProductEntity>> findProductResponse = productService.findProduct(
                new FindOneProductRequest(attachRequest.productId(), attachRequest.userId()));
        if (findProductResponse.isSuccess() == false)
            return Response.fromFailure(findProductResponse);
        if (findProductResponse.getPayload().isEmpty())
            return Response.fromFailure(new RepositoryFailures.ResourceNotFound(
                    "Usuário não contém o produto especificado. "));

        /**
         * Data Integrity: Validate category existence.
         */
        Response<Optional<CategoryEntity>> findCategoryResponse = categoryService
                .findCategory(new FindCategoryRequest(attachRequest.categoryId(), null));
        if (findCategoryResponse.isSuccess() == false)
            return Response.fromFailure(findCategoryResponse);
        if (findCategoryResponse.getPayload().isEmpty())
            return Response.fromFailure(new RepositoryFailures.ResourceNotFound(
                    "A categoria deseja não existe no sistema. "));

        /**
         * Create entity to be persisted.
         */
        ProductCategoryEntity entity = new ProductCategoryEntity(
                new ProductCategoryKey(attachRequest.productId(), attachRequest.categoryId()));

        /**
         * Execute persist operation & Handle exceptions.
         */
        try {
            entity = productCategoryRepository.save(Objects.requireNonNull(entity,
                    "Cannot persist null 'Product Category Entity' in 'Category Service' method 'attachCategory'"));
        } catch (NullPointerException e) {
            return Response.<ProductCategoryEntity>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        } catch (Exception e) {
            return Response.<ProductCategoryEntity>builder()
                    .fail(new Failures.UnhandledException(e)).build();
        }

        return Response.success(entity);
    }
}
