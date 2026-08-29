package com.github.noeeekr.servicerized.identity.repository.dao.configuration;

import org.hibernate.Transaction;

@FunctionalInterface
public interface GetTransactionCallback {
    Transaction get();
}
