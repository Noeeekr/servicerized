package com.github.noeeekr.servicerized.product.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.noeeekr.servicerized.product.repository.models.entities.CategoryEntity;
import com.github.noeeekr.servicerized.response.Response;
import com.github.noeeekr.servicerized.response.failure.Failures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CategoryRepository {
    public static final class CONSTANTS {
        public static final int MAX_CATEGORY_LISTING_LIMIT = 50;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Response<List<CategoryEntity>> findCategories() {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

        query.where(criteria
                .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));

        List<CategoryEntity> categories = new ArrayList<>();
        try {
            categories.addAll(entityManager.createQuery(query)
                    .setMaxResults(CategoryRepository.CONSTANTS.MAX_CATEGORY_LISTING_LIMIT)
                    .getResultList());
            return Response.success(categories);
        } catch (NoResultException e) {
            return Response.success(categories);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    }

    @Transactional
    public Response<List<CategoryEntity>> findCategory(UUID categoryId) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

        List<Predicate> requiredConditionals = new ArrayList<>();
        requiredConditionals.add(criteria
                .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
        requiredConditionals.add(criteria.equal(
                categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_ID), categoryId));
        query.where(criteria.and(requiredConditionals));

        List<CategoryEntity> categories = new ArrayList<>();
        try {
            categories.add(entityManager.createQuery(query).getSingleResult());
            return Response.success(categories);
        } catch (NoResultException e) {
            return Response.success(categories);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    };

    @Transactional
    public Response<List<CategoryEntity>> findCategories(String categoryName) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

        List<Predicate> requiredConditionals = new ArrayList<>();
        requiredConditionals.add(criteria
                .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
        requiredConditionals.add(
                criteria.like(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                        "%" + categoryName + "%"));
        query.where(criteria.and(requiredConditionals));

        List<CategoryEntity> categories = new ArrayList<>();
        try {
            categories.addAll(entityManager.createQuery(query).getResultList());
            return Response.success(categories);
        } catch (NoResultException e) {
            return Response.success(categories);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    };

    @Transactional
    public Response<List<CategoryEntity>> findCategory(String categoryName, UUID categoryId) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> query = criteria.createQuery(CategoryEntity.class);
        Root<CategoryEntity> categoryQuery = query.from(CategoryEntity.class);

        List<Predicate> requiredConditionals = new ArrayList<>();
        requiredConditionals.add(criteria
                .equal(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_DELETED_AT), null));
        requiredConditionals.add(
                criteria.like(categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_NAME),
                        "%" + categoryName + "%"));
        requiredConditionals.add(criteria.equal(
                categoryQuery.get(CategoryEntity.METADATA.COLUMN_NAME_CATEGORY_ID), categoryId));
        query.where(criteria.and(requiredConditionals));

        List<CategoryEntity> categories = new ArrayList<>();
        try {
            categories.add(entityManager.createQuery(query).getSingleResult());
            return Response.success(categories);
        } catch (NoResultException e) {
            return Response.success(categories);
        } catch (Exception e) {
            return Response.fromFailure(new Failures.UnhandledException(e));
        }
    };
}
