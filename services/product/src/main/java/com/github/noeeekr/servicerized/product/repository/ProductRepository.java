package com.github.noeeekr.servicerized.product.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.response.failure.Failures;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.entities.fields.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.product.repository.query.ProductRepositoryQueryBuilder;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

@Repository
public class ProductRepository implements ProductRepositoryQueryBuilder {
    @PersistenceContext
    private EntityManager entityManager;

    //
    // Interface implementations
    //
    @Override 
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    //
    // Create queries
    //
    public Response<ProductEntity> persist(ProductEntity product) {
        try {
            entityManager.persist(product);
            entityManager.flush();
            return Response.success(product);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }
    //
    // Select queries
    //

    /**
     * listProduct() finds a product by its ID.
     * 
     * @param productId the ID of the target product.
     * @return a default response object containing the product on success.
     */
    public Response<ProductEntity> listProduct(UUID productId) {
        ProductEntity product;
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                    "SELECT p FROM ProductEntity p WHERE p.id = :productId AND p.deletedAt = null",
                    ProductEntity.class);
            query.setParameter("productId", productId);
            product = query.getSingleResult();
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
        return Response.success(product);
    }

    /**
     * listProduct() finds a product by its ID and owner ID. Useful for checking if a products is
     * from a specific owner or not.
     * 
     * @param productId the ID of the target product.
     * @param ownerId the ID of the owner of the product.
     * @return a default response object containing the product on success.
     */
    public Response<ProductEntity> listProduct(UUID productId, UUID ownerId) {
        ProductEntity product;
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                    "SELECT p FROM ProductEntity p WHERE p.ownerId = :ownerId AND p.id = :productId AND p.deletedAt = null",
                    ProductEntity.class);
            query.setParameter("productId", productId);
            product = query.getSingleResult();
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
        return Response.success(product);
    }

    /**
     * Finds a list of products without any defined filter
     * 
     * @param limit a custom size for the list, defaults to 50 if > 50.
     * @return An ArrayList of products (ProductEntity)
     */
    public Response<List<ProductEntity>> listProducts(int limit) {
        List<ProductEntity> products;
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                    "SELECT p FROM ProductEntity p WHERE p.deletedAt = null", ProductEntity.class);
            products = query.getResultList();
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
        return Response.success(products);
    }

    /**
     * Finds a single product without any defined filter.
     * 
     * @return A product (ProductEntity)
     */
    public Response<ProductEntity> listProduct() {
        ProductEntity product;
        try {
            TypedQuery<ProductEntity> query = entityManager.createQuery(
                    "SELECT p FROM ProductEntity p WHERE p.deletedAt = null", ProductEntity.class);
            product = query.getSingleResult();
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
        return Response.success(product);
    }

    /**
     * Finds a single product by the following parameters:
     * 
     * - A single object of data the product must match. <br/>
     * - A list of categories the product must contain.
     * 
     * @param productFilter a object containing the fields that the product must match.
     * @param categoryFilters a list of objects containing the fields specifying the categories the
     *        product must contain.
     * @return A single product entity wrapped in a optional (to handle not found case) wrapped in
     *         the default response for this project (to handle failures).
     */
    public Response<Optional<ProductEntity>> listProduct(
            ProductFilterableFieldsInterface productFilter,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaQuery<ProductEntity> listProductQuery =
                this.buildListProductQuery(productFilter, categoryFilters);

        ProductEntity product;
        try {
            TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(listProductQuery);
            product = typedQuery.getSingleResult();
            return Response.success(Optional.of(product));
        } catch (NoResultException e) {
            return Response.success(Optional.empty());
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }

    }

    /**
     * Finds a list of products that match the parameters providen:
     * 
     * <ul>
     * <li>A single object of data the productmust match.</li>
     * <li>A list of categories the product must.</li>
     * </ul>
     * 
     * @param productFilter a object containing the fields that the product must match.
     * @param categoryFilters a list of objects containing the fields specifying the categories the
     *        product must contain.
     * @return A list of products wrapped in the default response for this project (to handle
     *         failures).
     */
    public Response<List<ProductEntity>> listProducts(
            ProductFilterableFieldsInterface productFilter,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaQuery<ProductEntity> listProductQuery =
                this.buildListProductQuery(productFilter, categoryFilters);

        List<ProductEntity> products;
        try {
            TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(listProductQuery);
            products = typedQuery.getResultList();
            return Response.success(products);
        } catch (NoResultException e) {
            return Response.success(new ArrayList<>());
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

}
