package com.github.noeeekr.servicerized.product.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.service.category.models.request.ListProductRelationRequest;
import com.github.noeeekr.servicerized.product.service.product.models.request.CreateProductCommandInterface;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.ResponseBuilder;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * findProduct() returns an optional with the product if found. If none are specified returns a
     * empty optional. If only one is specified also returns a empty optional.
     * 
     * @param filter The filters
     * @return a response containing a Optional with the product on success.
     */
    public Response<Optional<ProductEntity>> findProduct(ListProductRelationRequest filter) {
        ResponseBuilder<Optional<ProductEntity>> responseBuilder = Response.builder();
        /**
         * Validates if request is empty.
         */
        if (filter.getProductId() == null || filter.getProductOwnerId() == null) {
            return responseBuilder.success(Optional.empty()).build();
        }

        List<ProductEntity> products = new ArrayList<>();
        try {
            List<ProductEntity> fetchedProducts = productRepository.findProduct(
                    filter.getProductId(), filter.getProductOwnerId(), Limit.of(1));
            products.addAll(fetchedProducts);
        } catch (Exception e) {
            return responseBuilder.fail(new Failures.UnhandledException(e)).build();
        }

        /**
         * Validates if response is empty.
         */
        if (products.size() == 0) {
            return responseBuilder.success(Optional.empty()).build();
        }

        return responseBuilder.success(Optional.of(products.get(0))).build();
    }

    public Response<ProductEntity> createProduct(CreateProductCommandInterface product) {
        ProductEntity productEntity = ProductEntity.from(product);

        try {
            productEntity = productRepository
                    .save(Objects.requireNonNull(productEntity, "Product Entity Cannot be null"));
        } catch (Exception e) {
            return Response.<ProductEntity>builder().fail(new Failures.UnhandledException(e))
                    .build();
        }

        // return Response.<ProductEntity>builder().success(productEntity).build();
        return Response.<ProductEntity>builder().fail(new Failures.NotImplemented("Create Product",
                ProductService.class.getCanonicalName())).build();
    }
}
