package com.github.noeeekr.servicerized.identity.repository.dao.configuration;

import org.hibernate.Transaction;

public class DaoConfigurationBuilder {
    private Transaction tx = null;

    public static DaoConfigurationBuilder New() {
        return new DaoConfigurationBuilder();
    }

    public DaoConfigurationBuilder useTransaction(Transaction tx) {
        this.tx = tx;
        return this;
    }

    public DaoConfiguration build() {
        return new DaoConfiguration(tx);
    }
}
