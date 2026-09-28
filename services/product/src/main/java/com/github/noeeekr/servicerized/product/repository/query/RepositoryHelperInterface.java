package com.github.noeeekr.servicerized.product.repository.query;

import jakarta.persistence.EntityManager;

/**
 * RepositoryHelperInterface defines behavior that all interfaces that are meant to extend the
 * features of a repository class must implement. It acts as a bridge between repository data and
 * the interfaces default methods.
 */
abstract interface RepositoryHelperInterface {
    public EntityManager getEntityManager();
}
