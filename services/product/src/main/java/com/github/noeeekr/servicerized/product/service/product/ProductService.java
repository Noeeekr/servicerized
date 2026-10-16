package com.github.noeeekr.servicerized.product.service.product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.noeeekr.servicerized.product.failures.Failures;
import com.github.noeeekr.servicerized.product.repository.KindRepository;
import com.github.noeeekr.servicerized.product.repository.ProductKindRepository;
import com.github.noeeekr.servicerized.product.repository.ProductRepository;
import com.github.noeeekr.servicerized.product.repository.VirtualProductRepository;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductKindEntity;
import com.github.noeeekr.servicerized.product.repository.models.entities.VirtualProductEntity;
import com.github.noeeekr.servicerized.product.repository.models.relations.VirtualProductInformation;
import com.github.noeeekr.servicerized.product.service.product.models.command.CreateVirtualProductCommandInterface;
import com.github.noeeekr.servicerized.product.service.product.models.command.ListProductCommandInterface;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ListFilterInterface;
import com.github.noeeekr.servicerized.product.service.product.models.filters.ProductFilter;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    public static class LIMITS {
        public static int LIST = 20;
    }

    @Autowired
    private VirtualProductRepository virtualProductRepository;
    @Autowired
    private ProductKindRepository productKindRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KindRepository kindRepository;

    @Transactional
    public Response<List<ProductEntity>> listProducts(ListProductCommandInterface command) {
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

        Response<List<ProductEntity>> listProductResponse =
                productRepository.listProducts(productFilters, categoryFilters);
        if (listProductResponse.isSuccess() == false)
            return Response.fromFailure(listProductResponse);
        return listProductResponse;
    }


    @Transactional
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

    @Transactional
    public Response<Optional<ProductEntity>> listProduct(UUID productId, UUID ownerId) {
        return productRepository.listProduct(productId, ownerId);
    }

    @Transactional
    public Response<VirtualProductInformation> createVirtualProduct(
            CreateVirtualProductCommandInterface createCommand) {
        ProductEntity product = ProductEntity.from(createCommand);

        /**
         * Persist generic product object
         */
        Response<ProductEntity> persistProductRequest = productRepository
                .persist(Objects.requireNonNull(product, "Product Entity Cannot be null"));
        if (persistProductRequest.isSuccess() == false)
            return Response.fromFailure(persistProductRequest);
        product = persistProductRequest.getPayload();

        /**
         * Persist specific product kind object
         */
        KindEntity kind = kindRepository.getReferenceManager().getVirtualServiceKind();
        ProductKindEntity productKindEntity = new ProductKindEntity(product, kind);

        Response<ProductKindEntity> persistProductKindResponse =
                productKindRepository.persist(productKindEntity);
        if (persistProductKindResponse.isSuccess() == false)
            return Response.fromFailure(persistProductKindResponse);

        /**
         * Persist specific product kind data
         */
        Response<VirtualProductEntity> persistVirtualProductResponse = virtualProductRepository
                .persist(new VirtualProductEntity(product, createCommand.getProvisionHours()));
        if (persistVirtualProductResponse.isSuccess() == false)
            return Response.fromFailure(persistVirtualProductResponse);

        VirtualProductInformation virtualProductInformation = new VirtualProductInformation(
                persistVirtualProductResponse.getPayload(), persistProductRequest.getPayload(),
                KindEntity.Default.getVirtualServiceKind());
        return Response.success(virtualProductInformation);
    }

    @Transactional
    public Response<Integer> deleteVirtualProduct(ProductFilter filter) {

        if (filter.isEmpty())
            return Response
                    .fromFailure(new Failures.BadRequest("filtros de produto não definidos"));

        Response<Integer> markDeletedResponse = this.productRepository.markDeleted(filter);
        if (!markDeletedResponse.isSuccess())
            return markDeletedResponse;

        return markDeletedResponse;

    }
}
