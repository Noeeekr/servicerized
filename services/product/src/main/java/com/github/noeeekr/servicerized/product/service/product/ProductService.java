package com.github.noeeekr.servicerized.product.service.product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateProductCommandInterface;
import com.github.noeeekr.servicerized.product.service.product.models.command.ListProductCommandInterface;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilterInterface;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    public static class LIMITS {
        public static int LIST = 20;
    }

    @Autowired
    private ProductRepository productRepository;

    public Response<Optional<ProductEntity>> listProduct(ListProductCommandInterface command) {
        List<CategoryFilterableFieldsInterface> categoryFilters = null;
        ProductFilterableFieldsInterface productFilters = null;

        Optional<ListFilterInterface> listFilters = command.getFilter();
        if (listFilters.isEmpty() == false) {
            ListFilterInterface filters = listFilters.get();

            Optional<List<CategoryFilterableFieldsInterface>> categoryFilter =
                    filters.getCategoryFilter();
            if (categoryFilter.isEmpty() == false) {
                categoryFilters = categoryFilter.get();
            }

            Optional<ProductFilterableFieldsInterface> productFilter = filters.getProductFilter();
            if (productFilter.isEmpty() == false) {
                productFilters = productFilter.get();
            }
        }

        Response<Optional<ProductEntity>> listProductResponse =
                productRepository.listProduct(productFilters, categoryFilters);
        if (listProductResponse.isSuccess() == false)
            return Response.fromFailure(listProductResponse);
        return listProductResponse;
    }

    public Response<ProductEntity> createProduct(CreateProductCommandInterface product) {
        ProductEntity productEntity = ProductEntity.from(product);

        try {
            return productRepository
                    .save(Objects.requireNonNull(productEntity, "Product Entity Cannot be null"));
        } catch (Exception e) {
            return Response.<ProductEntity>builder().fail(new Failures.UnhandledException(e))
                    .build();
        }
    }
}
