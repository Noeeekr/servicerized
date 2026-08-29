package com.github.noeeekr.servicerized.identity.services.configuration;

import org.hibernate.Transaction;

public class ServiceConfigurationBuilder {
    private Transaction tx = null;

    public static ServiceConfigurationBuilder New() {
        return new ServiceConfigurationBuilder();
    }

    public ServiceConfigurationBuilder useTransaction(Transaction tx) {
        this.tx = tx;
        return this;
    }

    public ServiceConfiguration build() {
        return new ServiceConfiguration(tx);
    }
}
