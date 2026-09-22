package com.github.noeeekr.servicerized.product.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import com.github.noeeekr.servicerized.product.repository.interfaces.CreateProductInterface;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public Response<ProductEntity> createProduct(CreateProductInterface product) {
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
