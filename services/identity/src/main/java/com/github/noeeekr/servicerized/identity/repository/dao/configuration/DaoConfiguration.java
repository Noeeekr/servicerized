package com.github.noeeekr.servicerized.identity.repository.dao.configuration;

import org.hibernate.Transaction;

public class DaoConfiguration {
    private final Transaction tx;

    public DaoConfiguration() {
        this.tx = null;
    };

    public DaoConfiguration(Transaction tx) {
        this.tx = tx;
    };

    public Transaction getTransaction() {
        return this.tx;
    }

    public Transaction getTransaction(GetTransactionCallback callback) {
        if (this.tx == null) {
            return callback.get();
        }
        return this.tx;
    }
}
