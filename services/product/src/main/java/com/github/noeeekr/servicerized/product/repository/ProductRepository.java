package com.github.noeeekr.servicerized.product.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import com.github.noeeekr.servicerized.response.failure.Failures;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.CategoryFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.interfaces.filters.ProductFilterableFieldsInterface;
import com.github.noeeekr.servicerized.product.repository.models.ProductCategoryEntity;
import com.github.noeeekr.servicerized.product.repository.models.ProductEntity;
import com.github.noeeekr.servicerized.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //
    // Create queries
    //
    public Response<ProductEntity> save(ProductEntity product) {
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
    public Response<List<ProductEntity>> findProducts(int limit) {
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
     * Finds a product that match a single product template & multiple categories condition.
     * 
     * @param limit a custom size for the list, defaults to 50 if > 50.
     * @return An ArrayList of products (ProductEntity)
     */
    public Response<Optional<ProductEntity>> listProduct(
            ProductFilterableFieldsInterface productFilter,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = criteria.createQuery(ProductEntity.class);
        Root<ProductCategoryEntity> relationRoot = query.from(ProductCategoryEntity.class);

        List<Predicate> conditions = new ArrayList<>();

        if (categoryFilters != null && categoryFilters.isEmpty() != false) {
            conditions.add(this.getCategoryFiltersFor(relationRoot, categoryFilters));
        }
        if (productFilter != null) {
            conditions.add(this.getProductFiltersFor(relationRoot, productFilter));
        }

        conditions.add(criteria.equal(relationRoot.get("deletedAt"), null));

        ProductEntity product;
        try {
            TypedQuery<ProductEntity> typedQuery =
                    entityManager.createQuery(query.where(conditions));
            product = typedQuery.getSingleResult();
            return Response.success(Optional.of(product));
        } catch (NoResultException e) {
            return Response.success(Optional.empty());
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }

    }

    //
    // Helper functions
    //
    public Predicate getProductFiltersFor(Root<ProductCategoryEntity> relationRoot,
            ProductFilterableFieldsInterface productFilter) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        List<Predicate> productConditions = new ArrayList<>();
        Join<ProductCategoryEntity, ProductEntity> categoryJoin = relationRoot.join("category");

        if (!productFilter.getName().isEmpty() && !productFilter.getName().isBlank()) {
            Predicate predicate = criteria.equal(categoryJoin.get("name"), productFilter.getName());
            productConditions.add(predicate);
        }
        if (productFilter.getId() != null) {
            Predicate predicate = criteria.equal(categoryJoin.get("id"), productFilter.getId());
            productConditions.add(predicate);
        }

        return criteria.and(productConditions);
    }

    public Predicate getCategoryFiltersFor(Root<ProductCategoryEntity> relationRoot,
            List<CategoryFilterableFieldsInterface> categoryFilters) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        /**
         * Can possibly fail due to List iterator implementation
         */
        categoryFilters.removeIf((filter) -> {
            /**
             * Skips empty conditions
             */
            String categoryName = filter.getCategoryName();
            if (categoryName == null || categoryName.isEmpty())
                return true;
            return false;
        });

        List<Predicate> categoryConditions = new ArrayList<>();
        Join<ProductCategoryEntity, ProductEntity> categoryJoin = relationRoot.join("category");
        categoryFilters.forEach((filter) -> {
            Predicate predicate =
                    criteria.equal(categoryJoin.get("name"), filter.getCategoryName());
            categoryConditions.add(predicate);
        });

        return criteria.or(categoryConditions);
    }
}
